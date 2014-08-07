package phc.interfaces;

import java.util.Collection;
import java.util.List;

import android.content.Context;

import phc.objects.BloodTest;
import phc.objects.DocResult;
import phc.objects.Medicine;
import phc.objects.ScannedDoc;

public interface IDocStorage
{
	// Store the given document in the database, and return the saved document
	DocResult addDoc(ScannedDoc doc);
	// Delete the given document
	boolean deleteDoc(DocResult doc);
	// Return the doc with the specified id
	DocResult getDocById(String id);
	// Get the list of documents which are tagged with all the given tags
	// To get all documents, pass an empty collection of tags or null.
	Collection<DocResult> queryDocsByTags(Collection<String> tags);
	// Get the (flat) list of tags
	Collection<String> getExistingTags();
	// Get the child tags of a specific tag - pass null to get topmost level
	Collection<String> getChildTags(String tag);
	// Create new tag under specified parent (pass null as parent for top level tag)
	// Returns true on success, false on failure.
	boolean createTag(String name, String parent);
	// Remove all existing documents - only for debugging
	void clear();
	// Get history for a blood test, sorted by date
	List<BloodTest> getBloodTestHistory(Context context, String name);
	// Get medicine history associated with a blood test, sorted by date
	List<Medicine> getBloodTestAssociatedHistory(Context context, String testName);
	// Reset the database to the built-in one
	void resetDatabase(Context context);
}
