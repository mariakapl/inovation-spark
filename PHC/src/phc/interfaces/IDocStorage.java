package phc.interfaces;

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
}
