package phc.storage;

import java.util.ArrayList;

public class TagTree {

	private Node root;
	
    public TagTree(String tag_name) {
        root = new Node(tag_name);
        root.children = new ArrayList<Node>();
    }
    
    public Node findTag(String tag_name)
    {
    	if(root != null)
    		return root.findTag(tag_name);
    	return null;
    }
    
	public static class Node {

	    private String identifier;
	    private ArrayList<Node> children;

	    // Constructor
	    public Node(String identifier) {
	        this.identifier = identifier;
	        children = new ArrayList<Node>();
	    }

	    public Node findTag(String tag_name) {
			if(identifier == tag_name)
			{
				return this;
			}
			Node found = null; 
			for(Node node : children)
			{
				if(found == null)
					found = node.findTag(tag_name);
			}
			return found;
		}

		// Properties
	    public String getIdentifier() {
	        return identifier;
	    }

	    public ArrayList<Node> getChildren() {
	        return children;
	    }

	    // Public interface
	    public void addChild(String identifier) {
	        children.add(new Node(identifier));
	    }
	}
	
	
	
	
}
