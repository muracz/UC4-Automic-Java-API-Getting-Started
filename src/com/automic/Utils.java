package com.automic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.FolderTree;

public class Utils {

	private Connection connection;
	
	public Utils(Connection conn){
		this.connection = conn;
	}
	
	// Returns the Root Folder of the Client
	public IFolder getRootFolder() throws IOException{
		FolderTree tree = new FolderTree();
		this.connection.sendRequestAndWait(tree);
		return tree.root();		
	}
	
	// Returns a list of ALL Folders (including folders in folders, folders in folders in folders etc.)
	public ArrayList<IFolder> getAllFolders(boolean OnlyExtractFolderObjects) throws IOException{
		ArrayList<IFolder> FolderList = new ArrayList<IFolder>();
		// Below: Not including the RootFolder because it is actually not of type FOLD, but of type CLNT
		if(!OnlyExtractFolderObjects){FolderList.add(getRootFolder());}
		IFolder rootFolder = getRootFolder();
		Iterator<IFolder> it = rootFolder.subfolder();
		while (it.hasNext()){
			IFolder myFolder = it.next();
			if(! myFolder.getName().equals("<No Folder>")){
			addFoldersToList(FolderList,myFolder,OnlyExtractFolderObjects);
			}
		}
		return FolderList;
	}
	// Internal Method
	private void addFoldersToList(ArrayList<IFolder> folderList,
			IFolder myFolder, boolean onlyExtractFolderObjects) {
		if(onlyExtractFolderObjects){
			if( myFolder.getType().equals("FOLD")){folderList.add(myFolder);}
			if( myFolder.getType().equals("FOLD") && myFolder.subfolder() != null){
				
				Iterator<IFolder> it0 = myFolder.subfolder();
				while (it0.hasNext()){
					addFoldersToList(folderList,it0.next(),onlyExtractFolderObjects);
				}
				}
		}else{
			folderList.add(myFolder);
			if(  myFolder.subfolder() != null){
				Iterator<IFolder> it0 = myFolder.subfolder();
				while (it0.hasNext()){
					addFoldersToList(folderList,it0.next(),onlyExtractFolderObjects);
				}
				}
		}

	}
	
	// Returns a folder by name
	public IFolder getFolderByName(String FolderName) throws IOException{
		FolderTree tree = new FolderTree();
		this.connection.sendRequestAndWait(tree);
		return tree.getFolder(FolderName);	
	}
	
	// Returns a FolderList = the content of a given folder
	public FolderList getFolderContent(IFolder folder) throws IOException{
		FolderList objectsInRootFolder = new FolderList(folder);
		connection.sendRequestAndWait(objectsInRootFolder);
		return objectsInRootFolder;
	}
	
	// Same as above, requires only a folder name to run
	public FolderList getFolderContentByName(String FolderName) throws IOException{	
		return this.getFolderContent(this.getFolderByName(FolderName));
	}
}
