package Mindmap_Project;

import javax.swing.*;

public class Controller {

	private Text_Zone TextZone;
	private Mindmap_Zone MindZone;
	private Attribute_Zone AttributeZone;
	private JSplitPane MindZone_Pane;
	private Mind_Tree MindTree;
	private JSplitPane TextmapZone;
	private Mindmap Main;
	void set(Text_Zone textzone, Mindmap_Zone mindmapzone,Attribute_Zone attributezone,JSplitPane mindzonepane, Mind_Tree mindtree, JSplitPane textmapZone, Mindmap main){
		TextZone = textzone;
		MindZone = mindmapzone;
		AttributeZone = attributezone;
		MindZone_Pane = mindzonepane;
		MindTree = mindtree;
		TextmapZone = textmapZone;
		Main = main;
	}
	
	Mindmap_Zone getter_MindZone(){
		return MindZone;
	}
	
	Text_Zone getter_TextZone(){
		return TextZone;
	}
	
	Attribute_Zone getter_AttributeZone(){
		return AttributeZone;
	}
	
	JSplitPane getter_MindZone_pane(){
		return MindZone_Pane;
	}
	
	Mind_Tree getter_MindTree(){
		return MindTree;
	}
	
	JSplitPane getter_TextZone_pane(){
		return TextmapZone;
	}
	
	Mindmap getter_Mindmap(){
		return Main;
	}
}
