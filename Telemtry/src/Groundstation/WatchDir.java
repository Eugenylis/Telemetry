package Groundstation;
/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

/**
 * Class for creating a watchdog listener for any file additions and modifications in specified directory
 * Example to watch a directory (or tree) for changes to files.
 * 
 * Based upon:
 * WatchDir for directory watcher by Oracle
 * 
 * @author Erik Parker, Yevgeniy Lischuk
 * @version 2.0
 * 
 */
public class WatchDir implements Runnable{

	//declare a watcher
    private final WatchService watcher;
    //declare a mapper for watcher and path to file
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    //initialize variable to control the loop of directory watcher thread
    private boolean watching = true;
    
    //Suppress warnings
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    
    /**
     * Method to register the given directory with the WatchService
     * @throws IOException
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    
    /**
     * Method register the given directory, and all its sub-directories, with the WatchService
     * @throws IOException
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    
    /**
     * Creates a WatchService and registers the given directory
     * @throws IOException
     */
    WatchDir(Path dir, boolean recursive) throws IOException {
    	this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    
    /**
     * Mostly removed for use in a thread
     * @throws IOException
     */
    void processEvents() throws IOException {
    	
    }

    /**
     * Method to print out usage of WatchDir
     */
    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    
    /**
     * Implementation of the WatchDir thread
     * Process all events for keys queued to the watcher
     * Monitors specified directory for any file additions and changes
     * @override run method in thread
     */
	@Override
	public void run() {
		
		 while(watching) {

	        	// wait for key to be signaled
	            WatchKey key;
	            
	            try {
	            	key = watcher.take();
	            } catch (InterruptedException x) {
	                return;
	            }

	            Path dir = keys.get(key);
	            if (dir == null) {
	                System.err.println("WatchKey not recognized!!");
	                continue;
	            }
	            
	            for (WatchEvent<?> event: key.pollEvents()) {
	                WatchEvent.Kind kind = event.kind();

	                // TBD - provide example of how OVERFLOW event is handled
	                if (kind == OVERFLOW) {
	                    continue;
	                }

	                // Context for directory entry event is the file name of entry
	                WatchEvent<Path> ev = cast(event);
	                Path name = ev.context();
	                Path child = dir.resolve(name);

	                //Triggers when new file is created
	                if (event.kind().name() == ENTRY_CREATE.toString()){
	                	//System.out.format("%s: %s\n", event.kind().name(), child);
	                	
	                	//Adds file to list to be sent
	                	GS_Manager.zipTimer.addFile(child.toString());
	                
	                //Triggers when new file is modified
	                }else if (event.kind().name() == ENTRY_MODIFY.toString()){
	                	//System.out.format("%s\n", event.kind().name());
	                	
	                	//Adds file to list to be sent
	                	GS_Manager.zipTimer.addFile(child.toString());
	                	
	                }else { //Probably ENTRY_DELETE
	                    
	                	System.out.format("%s\n", event.kind().name());
	                	
	                }
	                
	                
	                // if directory is created, and watching recursively, then register it and its sub-directories
	                if (recursive && (kind == ENTRY_CREATE)) {
	                    try {
	                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
	                            registerAll(child);
	                        }
	                    } catch (IOException x) {
	                        // ignore to keep sample readable
	                    }
	                }
	            }

	            // reset key and remove from set if directory no longer accessible
	            boolean valid = key.reset();
	            if (!valid) {
	                keys.remove(key);

	                // all directories are inaccessible
	                if (keys.isEmpty()) {
	                    break;
	                }
	            }
	        }
	}
	
	
	/**
     * Method to set value used to control the loop inside the thread
     * watching - boolean value to determine if watcher thread needs to be executed
     * @param needWatch - true if need to monitor directory, false if not
     */
    public void setWatching(boolean needWatch){
    	this.watching = needWatch;
    }

    
    /**
     * Method to return value used to control the loop inside the thread
     * @return watching - boolean value to determine if watcher thread needs to be executed
     */
    public boolean getWatching(){
    	return this.watching;
    }
    
} // end of class