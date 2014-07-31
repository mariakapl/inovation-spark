package phc.interfaces;

import java.io.File;
import java.util.Collection;

import phc.objects.DocResult;
import phc.objects.ScannedDoc;

public interface IDocStorage
{
	// Store the given document in the database, and return the saved document
	DocResult addDoc(ScannedDoc doc);
	// Delete the given document
	boolean deleteDoc(DocResult doc);
	// Get the list of documents which are tagged with all the given tags
	// To get all documents, pass an empty collection of tags or null.
	Collection<DocResult> queryDocsByTags(Collection<String> tags);
	// Get the (flat) list of tags
	Collection<String> getExistingTags();
	//Get the child tags of a specific tag - pass null to get topmost level
	Collection<String> getChildTags(String tag);
}
