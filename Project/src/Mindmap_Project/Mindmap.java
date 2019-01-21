package Mindmap_Project;

import java.awt.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.util.*;
import java.lang.Math;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Mindmap extends JFrame {
	private static final long serialVersionUID = 1L;
	private Container Main_Space;
	private JPanel Main_Pane;
	private JSplitPane TextZone;
	private JSplitPane MindZone;

	private Controller controller;
	private Attribute_Zone AttributeZone;
	private Mindmap_Zone MindmapZone;
	private Text_Zone TextmapZone;
	private Mind_Tree MindTree;

	public static void main(String[] args) {
		new Mindmap();
	}

	Mindmap() {
		setTitle("무제");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controller = new Controller();
		AttributeZone = new Attribute_Zone();
		MindmapZone = new Mindmap_Zone();
		TextmapZone = new Text_Zone();
		MindTree = new Mind_Tree();

		TextZone = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		MindZone = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		controller.set(TextmapZone, MindmapZone, AttributeZone, MindZone, MindTree, TextZone, this);
		MindTree.setter_controller(controller);
		TextmapZone.setter_controller(controller);
		MindmapZone.setter_controller(controller);
		AttributeZone.setter_controller(controller);

		Main_Space();

		setSize(1280, 720);
		setVisible(true);

	}

	void Main_Space() {
		Main_Pane = new JPanel();
		Main_Space = getContentPane();
		setJMenuBar(new MenuBar(controller).get_menubar());
		set_GUI();
	}

	void set_GUI() {

		Main_Pane.setLayout(new BorderLayout());
		Main_Space.setLayout(new BorderLayout());
		Main_Space.add(new ToolBar(controller).get_toolbar(), BorderLayout.NORTH);
		TextZone.setDividerLocation(280);
		MindZone.setDividerLocation(680);

		MindZone.setRightComponent(AttributeZone.getter_attribute_zone());
		MindZone.setLeftComponent(MindmapZone.getter_mind_scroll());
		TextZone.setLeftComponent(TextmapZone.get_text_zone());
		TextZone.setRightComponent(MindZone);

		Main_Space.add(TextZone);
	}
}

class Text_Zone {
	static final int SUB_BASIC_COLOR = 0x00dbb5;
	private JPanel text, text_zone;
	private JTextArea textarea;
	private JScrollPane text_scroll;
	private JButton text_apply;

	Text_Zone() {
		text = new JPanel();
		text_zone = new JPanel();
		textarea = new JTextArea();
		textarea.setTabSize(5);
		textarea.setFont(new Font(null, Font.BOLD, 21));
		text_scroll = new JScrollPane(textarea);
		text_apply = new JButton("적용");
		text_apply.setBackground(Color.WHITE);
		text_apply.setForeground(Color.black);

		setting_item();
		adding_item();
	}

	JPanel getter_text() {
		return text;
	}

	JPanel getter_text_zone() {
		return text_zone;
	}

	JTextArea getter_textarea() {
		return textarea;
	}

	JScrollPane getter_text_scroll() {
		return text_scroll;
	}

	JButton getter_text_apply() {
		return text_apply;
	}

	JPanel get_text_zone() {
		return text_zone;
	}

	void setter_controller(Controller controller) {
		text_apply.addActionListener(new TextZone_apply_Listener(controller));
	}

	void setting_item() {
		text.setLayout(new BorderLayout());
		text_zone.setLayout(new BorderLayout());

		text_zone.setBackground(new Color(SUB_BASIC_COLOR));
		textarea.setBackground(new Color(SUB_BASIC_COLOR));
		text_scroll.setBackground(new Color(SUB_BASIC_COLOR));
	}

	void adding_item() {
		text.add(text_zone);
		text_zone.add(text_scroll);
		text_zone.add(text_apply, BorderLayout.SOUTH);
	}
}

class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Controller controller;
	DefaultMutableTreeNode root = null;

	MyPanel(Controller controller) {
		this.controller = controller;
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		if (root != null) {
			DefaultMutableTreeNode Child = null;
			Queue<TreeNode> BFS = new LinkedList<TreeNode>();
			int fx = 0, fy = 0, fh = 0, fw = 0, tx = 0, ty = 0, th, tw;
			BFS.offer(root);
			while (!BFS.isEmpty()) {
				DefaultMutableTreeNode q = (DefaultMutableTreeNode) BFS.poll();
				if (q.getChildCount() != 0) {
					fx = ((Mindmap_Label) (q.getUserObject())).getter_node().getX();
					fy = ((Mindmap_Label) (q.getUserObject())).getter_node().getY();
					fh = ((Mindmap_Label) (q.getUserObject())).getter_node().getHeight();
					fw = ((Mindmap_Label) (q.getUserObject())).getter_node().getWidth();

					for (int i = 0; i < q.getChildCount(); i++) {
						Child = (DefaultMutableTreeNode) q.getChildAt(i);
						tx = ((Mindmap_Label) Child.getUserObject()).getter_node().getX();
						ty = ((Mindmap_Label) Child.getUserObject()).getter_node().getY();
						th = ((Mindmap_Label) (Child.getUserObject())).getter_node().getHeight();
						tw = ((Mindmap_Label) (Child.getUserObject())).getter_node().getWidth();

						if (fy - fh >= ty) {
							g.drawLine(fx + fw / 2, fy, tx + tw / 2, ty + th);
							g.fillOval(tx + tw / 2 - 5, ty + th - 5, 10, 10);

						} else if (fy - fh <= ty && fy + fh >= ty && fx + fw / 2 < tx) {
							g.drawLine(fx + fw, fy + fh / 2, tx, ty + th / 2);
							g.fillOval(tx - 5, ty + th / 2 - 5, 10, 10);
						} else if (fy + fh <= ty) {
							g.drawLine(fx + fw / 2, fy + fh, tx + tw / 2, ty);
							g.fillOval(tx + tw / 2 - 5, ty - 5, 10, 10);
						} else {
							g.drawLine(fx, fy + fh / 2, tx + tw, ty + th / 2);
							g.fillOval(tx + tw - 5, ty + th / 2 - 5, 10, 10);
						}

					}
				}
				for (int i = 0; i < q.getChildCount(); i++) {
					BFS.offer((DefaultMutableTreeNode) q.getChildAt(i));
				}
			}

		}
		else
			repaint();
	}

	void setter_root(DefaultMutableTreeNode root) {
		this.root = root;
	}
}

class Mindmap_Zone {
	static final int BASIC_COLOR = 0x005c4c;
	private JPanel mindmap;
	private JScrollPane mind_scroll;
	private Controller controller;
	private DefaultMutableTreeNode root;

	Mindmap_Zone() {
		mindmap = new MyPanel(controller);
		mindmap.setLayout(null);
		mindmap.setPreferredSize(new Dimension(3000, 3000));
		mindmap.setLocation(-1000, -1000);
		mind_scroll = new JScrollPane(mindmap);
		setting_item();
	}

	JPanel getter_mindmap() {
		return mindmap;
	}

	JScrollPane getter_mind_scroll() {
		return mind_scroll;
	}

	DefaultMutableTreeNode getter_root() {
		return root;
	}

	void setter_mindmap(JPanel mindmap) {
		this.mindmap = mindmap;
	}

	void setting_item() {
		mindmap.setBackground(new Color(BASIC_COLOR));
	}

	void make_tree(DefaultMutableTreeNode filename) {

		mindmap.removeAll();
		mindmap.repaint();
		if (filename == null) {
			((MyPanel) (mindmap)).setter_root(filename);
			return;
		}
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) filename.getChildAt(0);
		int x = 0, y = 0;
		int r = root.getChildCount() * 30, preX = 1300, preY = 1290;
		int PLevel = 2;
		int childcount = root.getChildCount();
		int siblingcount = root.getChildCount();
		Color color;
		Mindmap_Label node;
		double theta = 0;
		DefaultMutableTreeNode sibling = root;
		DefaultMutableTreeNode[] Levelarr = new DefaultMutableTreeNode[root.getChildCount()];
		DefaultMutableTreeNode[] upLevelarr = new DefaultMutableTreeNode[root.getChildCount()];
		Queue<TreeNode> BFS = new LinkedList<TreeNode>();
		int parent_count = 0;
		double one_angle = 0, two_pie = Math.PI * 2;
		((Mindmap_Label) (filename.getUserObject())).setter_node(preX, preY, 80, 30);

		///////////////////////////////////////////////
		node = ((Mindmap_Label) (root.getUserObject()));
		node.setter_node(preX, preY, 80, 30);
		mindmap.add(node.getter_node());
		if (root.getChildCount() == 0) {
			((MyPanel) mindmap).setter_root(root);
			controller.getter_MindTree().setter_root(root);
			mindmap.repaint();
			return;
		}
		for (int i = 0; i < siblingcount; i++) {
			BFS.offer(root.getChildAt(i));
			upLevelarr[i] = Levelarr[i] = (DefaultMutableTreeNode) root.getChildAt(i);
		}
		double anglearr[] = new double[siblingcount];
		double makeanglearr[] = new double[siblingcount];
		if (root.getChildCount() % 2 == 1)
			anglearr[0] = two_pie * ((siblingcount - 1) / (double) siblingcount);
		else
			anglearr[0] = 0;
		int k = 0;
		for (int i = 1; i < siblingcount; i++) {
			anglearr[i] = anglearr[i - 1] + two_pie / siblingcount;
			if (anglearr[i] >= two_pie)
				anglearr[i] -= two_pie;
		}
		for (int i = 0; i < siblingcount; i++)
			makeanglearr[i] = anglearr[i];
		one_angle = two_pie;
		theta = anglearr[0] - two_pie / siblingcount;
		sibling = root;
		childcount = root.getChildCount();
		//////////////////////////////////////////////////
		while (!BFS.isEmpty()) {
			DefaultMutableTreeNode q = (DefaultMutableTreeNode) BFS.poll();
			int Level = q.getLevel();
			if (Level != PLevel) {
				sibling = (DefaultMutableTreeNode) (q.getParent());
				PLevel = Level;
				siblingcount = childcount;
				childcount = 0;
				int l = 0;
				Levelarr = new DefaultMutableTreeNode[upLevelarr.length];
				for (int i = 0; i < Levelarr.length; i++)
					Levelarr[i] = upLevelarr[i];

				for (int i = 0; i < Levelarr.length; i++) {
					childcount += Levelarr[i].getChildCount();
					if (Levelarr[i].equals(q.getParent())) {
						parent_count = i;
					}
				}
				upLevelarr = new DefaultMutableTreeNode[childcount];
				for (int i = 0; i < Levelarr.length; i++)
					for (int j = 0; j < Levelarr[i].getChildCount(); j++)
						upLevelarr[l++] = (DefaultMutableTreeNode) Levelarr[i].getChildAt(j);
				k = 0;
				sibling = (DefaultMutableTreeNode) (q.getParent());
				anglearr = new double[siblingcount];
				for (int i = 0; i < siblingcount; i++) {
					anglearr[i] = makeanglearr[i];
					if (anglearr[i] >= two_pie)
						anglearr[i] -= two_pie;
				}
				makeanglearr = new double[childcount];
				sibling = (DefaultMutableTreeNode) (q.getParent());

				if (parent_count == 0) {
					if (sibling.getChildCount() == 1)
						theta = anglearr[parent_count];
					else {
						if (anglearr[siblingcount - 1] > anglearr[0])
							theta = (anglearr[0] + anglearr[siblingcount - 1] + two_pie) / 2;
						else
							theta = (anglearr[0] + anglearr[siblingcount - 1]) / 2;
					}
					if (siblingcount == 1)
						one_angle = two_pie / 2;
					else {
						if ((anglearr[parent_count] + anglearr[parent_count + 1]) / 2 < theta)
							one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta + two_pie;
						else
							one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta;
					}

				} else if (parent_count == siblingcount - 1) {
					if (sibling.getChildCount() == 1)
						theta = anglearr[parent_count];
					else {
						if (anglearr[parent_count - 1] > anglearr[parent_count])
							theta = (anglearr[parent_count - 1] + anglearr[parent_count] + two_pie) / 2;
						else
							theta = (anglearr[parent_count - 1] + anglearr[parent_count]) / 2;
					}

					if (siblingcount == 1)
						one_angle = two_pie / 2;
					else {
						if ((anglearr[parent_count] + anglearr[0]) / 2 < theta)
							one_angle = (anglearr[parent_count] + anglearr[0] + two_pie) / 2 - theta;
						else
							one_angle = (anglearr[parent_count] + anglearr[0]) / 2 - theta;
					}

				} else {
					if (sibling.getChildCount() == 1)
						theta = anglearr[parent_count];
					else {
						if (anglearr[parent_count - 1] > anglearr[parent_count])
							theta = (anglearr[parent_count - 1] + anglearr[parent_count] + two_pie) / 2;
						else
							theta = (anglearr[parent_count - 1] + anglearr[parent_count]) / 2;
					}

					if (siblingcount == 1)
						one_angle = two_pie / 2;
					else {
						if ((anglearr[parent_count] + anglearr[parent_count + 1]) / 2 < theta)
							one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta + two_pie;
						else
							one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta;
					}
				}

				makeanglearr[k] = theta;
				k++;
				r += siblingcount * 30;
			} else {
				if (q.getParent() != sibling) {
					sibling = (DefaultMutableTreeNode) q.getParent();

					for (int i = 0; i < Levelarr.length; i++) {
						if (Levelarr[i].equals(q.getParent())) {
							parent_count = i;
						}
					}

					sibling = (DefaultMutableTreeNode) q.getParent();
					if (parent_count == 0) {
						if (sibling.getChildCount() == 1)
							theta = anglearr[parent_count];
						else {
							if (anglearr[siblingcount - 1] > anglearr[0])
								theta = (anglearr[0] + anglearr[siblingcount - 1] + two_pie) / 2;
							else
								theta = (anglearr[0] + anglearr[siblingcount - 1]) / 2;
						}

						if (siblingcount == 1)
							one_angle = two_pie / 2;
						else {
							if ((anglearr[parent_count] + anglearr[parent_count + 1]) / 2 < theta)
								one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta + two_pie;
							else
								one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta;
						}

					} else if (parent_count == siblingcount - 1) {
						if (sibling.getChildCount() == 1)
							theta = anglearr[parent_count];
						else {
							if (anglearr[parent_count - 1] > anglearr[parent_count])
								theta = (anglearr[parent_count - 1] + anglearr[parent_count] + two_pie) / 2;
							else
								theta = (anglearr[parent_count - 1] + anglearr[parent_count]) / 2;
						}

						if (siblingcount == 1)
							one_angle = two_pie / 2;
						else {
							if ((anglearr[parent_count] + anglearr[0]) / 2 < theta)
								one_angle = (anglearr[parent_count] + anglearr[0] + two_pie) / 2 - theta;
							else
								one_angle = (anglearr[parent_count] + anglearr[0]) / 2 - theta;
						}

					} else {
						if (sibling.getChildCount() == 1)
							theta = anglearr[parent_count];
						else {
							if (anglearr[parent_count - 1] > anglearr[parent_count])
								theta = (anglearr[parent_count - 1] + anglearr[parent_count] + two_pie) / 2;
							else
								theta = (anglearr[parent_count - 1] + anglearr[parent_count]) / 2;
						}

						if (siblingcount == 1)
							one_angle = two_pie / 2;
						else {
							if ((anglearr[parent_count] + anglearr[parent_count + 1]) / 2 < theta)
								one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta + two_pie;
							else
								one_angle = (anglearr[parent_count] + anglearr[parent_count + 1]) / 2 - theta;
						}

					}
					makeanglearr[k++] = theta;

				} else {
					theta += one_angle / (double) sibling.getChildCount();
					makeanglearr[k++] = theta;
				}
			}
			x = (int) (r * Math.sin(theta));
			y = (int) (r * Math.cos(theta));
			Mindmap_Label Parent_Label = (Mindmap_Label) sibling.getUserObject();
			Mindmap_Label current_Label = (Mindmap_Label) q.getUserObject();
			color = Parent_Label.getter_node().getBackground();
			Color node_color;
			try {
				node_color = new Color(color.getRed() - 70, color.getGreen() - 70, color.getBlue() - 70);
			} catch (IllegalArgumentException e) {
				node_color = new Color(0, 0, 0);
			}
			current_Label.setter_node(preX + x, preY + y, 80, 30, node_color);
			mindmap.add(current_Label.getter_node());
			for (int i = 0; i < q.getChildCount(); i++) {
				BFS.offer((DefaultMutableTreeNode) q.getChildAt(i));
			}
		}
		((MyPanel) mindmap).setter_root(root);
		controller.getter_MindTree().setter_root(root);
		mindmap.repaint();
	}

	void setter_controller(Controller controller) {
		this.controller = controller;
		mindmap.addMouseListener(new Other_Location_Click_Listener(controller));
	}

}

class Other_Location_Click_Listener extends MouseAdapter {
	private Controller controller;

	Other_Location_Click_Listener(Controller controller) {
		this.controller = controller;
	}

	@SuppressWarnings("deprecation")
	public void mouseClicked(MouseEvent e) {

		if (controller.getter_AttributeZone().getter_Mindmap_Label() != null) {
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_up_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_down_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_left_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_right_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_ne_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_nw_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_se_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_sw_label().show(false);
		}

		controller.getter_AttributeZone().reset_attribute();
	}
}

class Attribute_Zone {
	static final int SUB_BASIC_COLOR = 0x00dbb5;
	private JPanel attribute, attribute_zone;
	private JScrollPane attribute_scroll;
	private JTextField x_var, y_var, w_var, h_var, node_var, color_var;
	private JScrollPane node_var_scroll;
	private JButton attribute_change;
	private Mindmap_Label curser = null;
	private Controller controller;
	private Color c = Color.WHITE;

	Attribute_Zone() {
		attribute = new JPanel();
		attribute_scroll = new JScrollPane(attribute);
		attribute_zone = new JPanel();
		attribute_change = new JButton("변경");
		attribute_change.setBackground(Color.WHITE);
		attribute_change.setForeground(Color.black);
		node_var = new JTextField();
		x_var = new JTextField();
		y_var = new JTextField();
		w_var = new JTextField();
		h_var = new JTextField();
		color_var = new JTextField();
		color_var.setEnabled(false);
		node_var_scroll = new JScrollPane(node_var);

		setting_item();
		adding_item();
	}

	void reset_attribute() {
		node_var.setText("");
		x_var.setText("");
		y_var.setText("");
		h_var.setText("");
		w_var.setText("");
		color_var.setText("");

	}

	JPanel getter_attribute_zone() {
		return attribute_zone;
	}

	JPanel getter_attribute() {
		return attribute;
	}

	JScrollPane getter_attribute_scroll() {
		return attribute_scroll;
	}

	JTextField getter_x_var() {
		return x_var;
	}

	JTextField getter_y_var() {
		return y_var;
	}

	JTextField getter_w_var() {
		return w_var;
	}

	JTextField getter_h_var() {
		return h_var;
	}

	JTextField getter_node_var() {
		return node_var;
	}

	JTextField getter_color_var() {
		return color_var;
	}

	Mindmap_Label getter_Mindmap_Label() {
		return curser;
	}

	Color getter_color() {
		return c;
	}

	void setter_Mindmap_Label(Mindmap_Label node) {
		curser = node;
	}

	void setting_item() {
		attribute.setBackground(new Color(SUB_BASIC_COLOR));
		attribute.setLayout(new GridLayout(6, 2, 0, 50));
		attribute_zone.setBackground(new Color(SUB_BASIC_COLOR));
		attribute_zone.setLayout(new BorderLayout());
	}

	void adding_item() {
		node_var.setEnabled(false);
		node_var.setBackground(Color.lightGray.brighter());
		attribute.add(Attribute_Label("Text"));
		attribute.add(node_var_scroll);
		attribute.add(Attribute_Label("X"));
		attribute.add(x_var);
		attribute.add(Attribute_Label("Y"));
		attribute.add(y_var);
		attribute.add(Attribute_Label("W"));
		attribute.add(w_var);
		attribute.add(Attribute_Label("H"));
		attribute.add(h_var);
		attribute.add(Attribute_Label("Color"));
		attribute.add(color_var);

		attribute_zone.add(attribute_scroll);
		attribute_zone.add(attribute_change, BorderLayout.SOUTH);
	}

	private JLabel Attribute_Label(String name) {
		JLabel text = new JLabel(name + " : ");
		text.setFont(text.getFont().deriveFont(24.0f));
		text.setHorizontalAlignment(SwingConstants.RIGHT);

		return text;
	}

	void setter_controller(Controller controller) {
		this.controller = controller;
		color_var.addMouseListener(new attribute_color_var_Listener());
		attribute_change.addActionListener(new Attribute_change_Listener(controller));
	}

	class attribute_color_var_Listener extends MouseAdapter {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getClickCount() == 2) {
				color_chooser();
			}
		}
	}

	void color_chooser() {
		c = JColorChooser.showDialog(null, "Choose a Color", Color.WHITE);
		try {
			if (c != null) {
				controller.getter_AttributeZone().getter_Mindmap_Label().getter_node().setBackground(c);
				Color reverse = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
				controller.getter_AttributeZone().getter_Mindmap_Label().getter_node().setForeground(reverse);
				color_var.setHorizontalAlignment(SwingConstants.CENTER);
				color_var.setText(Integer.toHexString(c.getRGB()).substring(2, 8));
				color_var.setFont(new Font(null, Font.BOLD, 30));
				c = null;
			}
		} catch (NullPointerException e) {

		}
	}
}

class Attribute_change_Listener implements ActionListener {
	private Controller controller;

	public Attribute_change_Listener(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			int x = Integer.parseInt(controller.getter_AttributeZone().getter_x_var().getText());
			int y = Integer.parseInt(controller.getter_AttributeZone().getter_y_var().getText());
			int w = Integer.parseInt(controller.getter_AttributeZone().getter_w_var().getText());
			int h = Integer.parseInt(controller.getter_AttributeZone().getter_h_var().getText());
			Color c = controller.getter_AttributeZone().getter_Mindmap_Label().getter_node().getBackground();
			controller.getter_AttributeZone().getter_Mindmap_Label().setter_node(x + 1000, y + 1000, w, h, c);
			controller.getter_AttributeZone().getter_Mindmap_Label().vector_label_location();
			controller.getter_MindZone().getter_mindmap().revalidate();
			controller.getter_MindZone().getter_mindmap().repaint();

		} catch (NumberFormatException e1) {
			new Dialog("노드를 선택하세요.", controller);
		}
	}
}

@SuppressWarnings("serial")
class File_Chooser extends JFrame {
	private JFileChooser file_chooser;
	private String file_name = null;
	private Controller controller;
	private int result;
	private JSONObject json_obj;
	private JSONArray json_arr;
	private JSONParser parser;
	private DefaultMutableTreeNode root;

	File_Chooser(Controller controller) {
		this.controller = controller;

		if (file_name != null) {
			String dir = file_name;
			file_chooser = new JFileChooser(dir);
		} else
			file_chooser = new JFileChooser();

		json_obj = new JSONObject();
		json_arr = new JSONArray();
		parser = new JSONParser();
		root = new DefaultMutableTreeNode();
		file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		file_chooser.setMultiSelectionEnabled(false);
		file_chooser.addChoosableFileFilter(new Json_Filter(".json", "JSON FILES"));

	}

	void open_file() throws ParseException {
		result = file_chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {// 파일을 열었을 때
			file_name = file_chooser.getSelectedFile().getPath();
			Object obj = null;
			try {
				try {
					obj = parser.parse(new FileReader(file_name));
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				controller.getter_Mindmap().setTitle(file_name);

				JSONObject jsonObject = (JSONObject) obj;
				JSONArray json_arr = (JSONArray) jsonObject.get("node_info");
				String node = (String) jsonObject.get("textarea");

				controller.getter_TextZone().getter_textarea().setText(node);
				controller.getter_TextZone().getter_text_apply().doClick();
				root = controller.getter_MindTree().getter_root();
				if (root != null) {
					DefaultMutableTreeNode q = null;
					Queue<TreeNode> BFS = new LinkedList<TreeNode>();
					int j = 0;
					BFS.offer(root);

					while (!BFS.isEmpty()) {
						q = (DefaultMutableTreeNode) BFS.poll();

						JSONObject node_Object = (JSONObject) json_arr.get(j++);
						Long x_var = (Long) node_Object.get("x_var");
						Long y_var = (Long) node_Object.get("y_var");
						Long w_var = (Long) node_Object.get("w_var");
						Long h_var = (Long) node_Object.get("h_var");
						Long color_var = (Long) node_Object.get("color_var");

						((Mindmap_Label) (q.getUserObject())).setter_node(x_var.intValue(), y_var.intValue(),
								w_var.intValue(), h_var.intValue(), new Color(color_var.intValue()));

						for (int i = 0; i < q.getChildCount(); i++) {
							BFS.offer((DefaultMutableTreeNode) q.getChildAt(i));
						}
					}

					controller.getter_MindZone().getter_mindmap().revalidate();
					controller.getter_MindZone().getter_mindmap().repaint();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (StringIndexOutOfBoundsException e) {

			}

		} else
			return;
	}

	void save_file() {

		int r = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?", "저장하기", JOptionPane.YES_NO_OPTION);

		if (r == 0) {
			if (controller.getter_Mindmap().getTitle() != "무제") {

				make_info();
				FileWriter file;
				try {
					file = new FileWriter(controller.getter_Mindmap().getTitle());

					file.write(json_obj.toJSONString());
					file.flush();
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				saveas_file();
			}
		}
	}

	void saveas_file() {

		result = file_chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			this.file_name = file_chooser.getSelectedFile().getPath();
			controller.getter_Mindmap().setTitle(file_name + ".json");
			make_info();
			FileWriter file;
			try {
				file = new FileWriter(controller.getter_Mindmap().getTitle());

				file.write(json_obj.toJSONString());
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file_name = null;
		}
	}

	@SuppressWarnings("unchecked")
	void make_info() {

		json_arr.removeAll(json_arr);
		json_obj.put("textarea", controller.getter_TextZone().getter_textarea().getText());
		root = controller.getter_MindTree().getter_root();

		if (root != null) {
			Queue<TreeNode> BFS = new LinkedList<TreeNode>();
			int x = 0, y = 0, w = 0, h = 0, color = 0;

			BFS.offer(root);

			while (!BFS.isEmpty()) {
				DefaultMutableTreeNode q = (DefaultMutableTreeNode) BFS.poll();
				x = ((Mindmap_Label) (q.getUserObject())).getter_node().getX();
				y = ((Mindmap_Label) (q.getUserObject())).getter_node().getY();
				h = ((Mindmap_Label) (q.getUserObject())).getter_node().getHeight();
				w = ((Mindmap_Label) (q.getUserObject())).getter_node().getWidth();
				color = ((Mindmap_Label) (q.getUserObject())).getter_node().getBackground().getRGB();

				JSONObject node_obj = new JSONObject();
				node_obj.put("x_var", x);
				node_obj.put("y_var", y);
				node_obj.put("h_var", h);
				node_obj.put("w_var", w);
				node_obj.put("color_var", color);
				json_arr.add(node_obj);

				for (int i = 0; i < q.getChildCount(); i++) {
					BFS.offer((DefaultMutableTreeNode) q.getChildAt(i));
				}
			}
			json_obj.put("node_info", json_arr);
		}

	}

	class Json_Filter extends javax.swing.filechooser.FileFilter {
		String type;
		String desc;

		Json_Filter(final String type, final String desc) {
			this.type = type;
			this.desc = desc;
		}

		public boolean accept(File f) {
			return f.getName().endsWith(type) || f.isDirectory();
		}

		public String getDescription() {
			return desc;
		}
	}
}

class File_Chooser_Listener implements ActionListener {
	private Controller controller;

	File_Chooser_Listener(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "열기") {
			try {
				new File_Chooser(controller).open_file();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand() == "다른 이름으로 저장") {
			new File_Chooser(controller).saveas_file();
		} else if (e.getActionCommand() == "저장") {
			new File_Chooser(controller).save_file();
		} else if (e.getActionCommand() == "새로만들기") { // 새로만들기 선택시 이벤트
			int r = JOptionPane.showConfirmDialog(null, "새로 만드시겠습니까?", "새로만들기", JOptionPane.YES_NO_OPTION);
			if (r == 0) {
				new File_Chooser(controller).save_file();
				controller.getter_TextZone().getter_textarea().setText("");
				controller.getter_Mindmap().setTitle("무제");

				controller.getter_AttributeZone().getter_node_var().setText("");
				controller.getter_AttributeZone().getter_x_var().setText("");
				controller.getter_AttributeZone().getter_y_var().setText("");
				controller.getter_AttributeZone().getter_h_var().setText("");
				controller.getter_AttributeZone().getter_w_var().setText("");
				controller.getter_AttributeZone().getter_color_var().setText("");

				controller.getter_MindTree().setter_root(null);
				((MyPanel) (controller.getter_MindZone().getter_mindmap())).setter_root(null);
				controller.getter_MindZone().getter_mindmap().removeAll();
				controller.getter_MindZone().getter_mindmap().repaint();
			}
		}

		else if (e.getActionCommand() == "닫기") {
			new File_Chooser(controller).save_file();
			System.exit(0);
		}
	}
}

class ToolBar {
	private JToolBar toolbar;
	private JButton change_attribute, new_file, file_open, save_file, saveas_file, close_file, tool_apply; // 필수
	private JButton change_color, show_tree;
	private Controller controller;

	ToolBar(Controller controller) {
		this.controller = controller;
		toolbar = new JToolBar("MindMap Menu");
		new_file = new JButton("새로만들기");
		file_open = new JButton("열기");
		save_file = new JButton("저장");
		saveas_file = new JButton("다른 이름으로 저장");
		close_file = new JButton("닫기");
		tool_apply = new JButton("적용");
		change_attribute = new JButton("변경");
		change_color = new JButton("색교체");
		show_tree = new JButton("트리 보기");

		setting_item();
		adding_item();
		action_item();
	}

	JToolBar get_toolbar() {
		return toolbar;
	}

	void setting_item() {
		toolbar.setBackground(Color.LIGHT_GRAY);
	}

	void adding_item() {
		toolbar.add(new_file);
		toolbar.add(file_open);
		toolbar.add(save_file);
		toolbar.add(saveas_file);
		toolbar.add(close_file);
		toolbar.add(tool_apply);
		toolbar.add(change_attribute);
		toolbar.add(change_color);
		toolbar.add(show_tree);
	}

	void action_item() {
		close_file.addActionListener(new File_Chooser_Listener(controller));
		new_file.addActionListener(new File_Chooser_Listener(controller));
		save_file.addActionListener(new File_Chooser_Listener(controller));
		saveas_file.addActionListener(new File_Chooser_Listener(controller));
		file_open.addActionListener(new File_Chooser_Listener(controller));
		change_attribute.addActionListener(new Attribute_change_Listener(controller));
		change_color.addActionListener(new Change_color_Listener(controller));
		tool_apply.addActionListener(new TextZone_apply_Listener(controller));
		show_tree.addActionListener(new Show_Tree_Click_Listener(controller));
	}
}

class MenuBar {
	static final int BASIC_COLOR = 0x005c4c;
	private JMenuBar menubar;
	private JMenu FileMenu, SaveMenu, ApplyMenu, Show_TreeMenu;
	private JMenuItem file_open, file_close, file_new, save_save, save_saveas, apply_apply, apply_change,
			apply_change_color, show_tree, show_textarea;
	private Controller controller;

	MenuBar(Controller controller) {
		this.controller = controller;
		menubar = new JMenuBar();
		FileMenu = new JMenu("파일");
		SaveMenu = new JMenu("저장");
		ApplyMenu = new JMenu("적용/변경");
		file_new = new JMenuItem("새로만들기");
		file_open = new JMenuItem("열기");
		file_close = new JMenuItem("닫기");
		save_save = new JMenuItem("저장");
		save_saveas = new JMenuItem("다른 이름으로 저장");
		apply_apply = new JMenuItem("적용");
		apply_change = new JMenuItem("변경");
		apply_change_color = new JMenuItem("색교체");
		Show_TreeMenu = new JMenu("트리 보기");
		show_tree = new JMenuItem("트리 보기");
		show_textarea = new JMenuItem("텍스트입력창 보기");

		setting_item();
		adding_item();
		action_item();
	}

	JMenuBar get_menubar() {
		return menubar;
	}

	void setting_item() {

	}

	void adding_item() {
		FileMenu.add(file_new);
		FileMenu.add(file_open);
		FileMenu.addSeparator();
		FileMenu.add(file_close);
		menubar.add(FileMenu);

		SaveMenu.add(save_save);
		SaveMenu.add(save_saveas);
		menubar.add(SaveMenu);

		ApplyMenu.add(apply_apply);
		ApplyMenu.addSeparator();
		ApplyMenu.add(apply_change);
		ApplyMenu.add(apply_change_color);

		menubar.add(ApplyMenu);

		Show_TreeMenu.add(show_tree);
		Show_TreeMenu.addSeparator();
		Show_TreeMenu.add(show_textarea);
		menubar.add(Show_TreeMenu);
	}

	void action_item() {
		show_textarea.addActionListener(new Menu_Show_Tree_Listener());
		show_tree.addActionListener(new Menu_Show_Tree_Listener());
		save_save.addActionListener(new File_Chooser_Listener(controller));
		save_saveas.addActionListener(new File_Chooser_Listener(controller));
		file_open.addActionListener(new File_Chooser_Listener(controller));
		file_close.addActionListener(new File_Chooser_Listener(controller));
		file_new.addActionListener(new File_Chooser_Listener(controller));
		apply_change.addActionListener(new Attribute_change_Listener(controller));
		apply_change_color.addActionListener(new Change_color_Listener(controller));
		apply_apply.addActionListener(new TextZone_apply_Listener(controller));
	}

	class Menu_Show_Tree_Listener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				if (e.getSource() == show_tree) {

					controller.getter_TextZone_pane().setLeftComponent(controller.getter_MindTree().show_tree());
					controller.getter_TextZone_pane().setDividerLocation(280);

				} else {
					controller.getter_TextZone().adding_item();
					controller.getter_TextZone_pane().setLeftComponent(controller.getter_TextZone().getter_text_zone());
					controller.getter_TextZone_pane().setDividerLocation(280);
				}
			} catch (StringIndexOutOfBoundsException e1) {
			}
		}

	}
}

class Show_Tree_Click_Listener implements ActionListener {
	private Controller controller;
	private JButton change;

	public Show_Tree_Click_Listener(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {

		change = (JButton) e.getSource();

		try {
			if (change.getText().equals("트리 보기")) {

				controller.getter_TextZone_pane().setLeftComponent(controller.getter_MindTree().show_tree());
				controller.getter_TextZone_pane().setDividerLocation(280);
				change.setText("텍스트입력창 보기");

			} else {
				controller.getter_TextZone().adding_item();
				controller.getter_TextZone_pane().setLeftComponent(controller.getter_TextZone().getter_text_zone());
				controller.getter_TextZone_pane().setDividerLocation(280);
				change.setText("트리 보기");
			}
		} catch (StringIndexOutOfBoundsException e1) {
		}
	}
}

class Change_color_Listener implements ActionListener {
	static final int BASIC_COLOR = 0x005c4c;
	private Controller controller;

	Change_color_Listener(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {
		Color c = JColorChooser.showDialog(null, "Choose a Color", new Color(BASIC_COLOR));
		if (c != null) {
			controller.getter_MindZone().getter_mindmap().setBackground(c);

			controller.getter_AttributeZone().getter_attribute_zone().setBackground(c.brighter().brighter());
			controller.getter_AttributeZone().getter_attribute_scroll().setBackground(c.brighter().brighter());
			controller.getter_AttributeZone().getter_attribute().setBackground(c.brighter().brighter());

			controller.getter_TextZone().getter_text().setBackground(c.brighter().brighter());
			controller.getter_TextZone().getter_text_scroll().setBackground(c.brighter().brighter());
			controller.getter_TextZone().getter_textarea().setBackground(c.brighter().brighter());
			controller.getter_TextZone().getter_text_zone().setBackground(c.brighter().brighter());

		}
	}
}

class TextZone_apply_Listener implements ActionListener {
	private Controller controller;

	public TextZone_apply_Listener(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {

		try {
			controller.getter_MindTree().set_tree();
		} catch (StringIndexOutOfBoundsException e1) {
			controller.getter_MindZone().getter_mindmap().removeAll();
			controller.getter_MindZone().getter_mindmap().repaint();
		}
	}
}

class Mind_Tree {
	protected Controller controller;
	private Mindmap_Label lb;
	protected DefaultMutableTreeNode root;
	private DefaultMutableTreeNode curser;
	private JTree tree;
	private String text;
	static final int SUB_BASIC_COLOR = 0x00dbb5;

	protected int Level = 0;

	Mind_Tree() {

	}

	Mind_Tree(Controller controller) {
		this.controller = controller;

	}

	void setter_controller(Controller controller) {
		this.controller = controller;
	}

	DefaultMutableTreeNode getter_root() {
		return root;
	}

	void setter_root(DefaultMutableTreeNode root) {
		this.root = root;
	}

	JScrollPane show_tree() {

		tree = new JTree(root);
		tree.addTreeSelectionListener(new node_Clicked());
		Level = 1;
		JPanel tree_pane = new JPanel();
		JScrollPane tree_scroll = new JScrollPane(tree_pane);
		tree_pane.setLayout(new BorderLayout());
		tree_pane.add(tree);
		tree.setBackground(controller.getter_TextZone().getter_text_scroll().getBackground());
		return tree_scroll;
	}

	void set_tree() {
		root = null;
		make_tree();
		controller.getter_MindZone().make_tree(root);
	}

	void make_tree() {

		lb = new Mindmap_Label("FileName", controller);
		root = new DefaultMutableTreeNode(lb);
		curser = root;
		text = controller.getter_TextZone().getter_textarea().getText();
		if(text.length()==0)
			text+='\0';
		if (text.charAt(text.length() - 1) != '\n')
			text += '\n';
		text += '\0';
		int i = 0;
		Level = 1;
		String name = "";
		int curLv = 0;

		while (text.charAt(i) != '\0') {
			if (text.charAt(i) == '\n') {
				for (int j = Level; j <= curLv; j++)
					curser = (DefaultMutableTreeNode) curser.getParent();
				Mindmap_Label node = new Mindmap_Label(name, controller);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(node);
				curser.add(child);
				curser = child;
				name = "";
				curLv = Level;
				Level = 1;
			} else if (text.charAt(i) == '\t')
				Level++;
			else {
				name += text.charAt(i);
			}
			i++;
		}
		if(root.getChildCount()==0)
			setter_root(null);
	}

	class node_Clicked implements TreeSelectionListener {
		@SuppressWarnings("deprecation")
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getSource() == tree) {

				curser = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (curser == null)
					return;
				TreeNode[] arr = curser.getPath();
				DefaultMutableTreeNode path = root;
				for (TreeNode i : arr) {
					for (int j = 0; j < path.getChildCount(); j++) {
						if (path.getChildAt(j).equals(i)) {
							path = (DefaultMutableTreeNode) path.getChildAt(j);
							break;
						}
					}
				}
				((Mindmap_Label) (path.getUserObject())).setting_attribute();
				JLabel up_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_up_label();
				JLabel down_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_down_label();
				JLabel left_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_left_label();
				JLabel right_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_right_label();
				JLabel ne_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_ne_label();
				JLabel nw_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_nw_label();
				JLabel se_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_se_label();
				JLabel sw_label = controller.getter_AttributeZone().getter_Mindmap_Label().getter_sw_label();

				if (up_label.isShowing() == true) {
					up_label.show(false);
					down_label.show(false);
					left_label.show(false);
					right_label.show(false);
					ne_label.show(false);
					nw_label.show(false);
					se_label.show(false);
					sw_label.show(false);
				} else {
					up_label.show(true);
					down_label.show(true);
					left_label.show(true);
					right_label.show(true);
					ne_label.show(true);
					nw_label.show(true);
					se_label.show(true);
					sw_label.show(true);
				}
				controller.getter_MindZone().getter_mindmap().add(up_label);
				controller.getter_MindZone().getter_mindmap().add(down_label);
				controller.getter_MindZone().getter_mindmap().add(left_label);
				controller.getter_MindZone().getter_mindmap().add(right_label);
				controller.getter_MindZone().getter_mindmap().add(ne_label);
				controller.getter_MindZone().getter_mindmap().add(nw_label);
				controller.getter_MindZone().getter_mindmap().add(se_label);
				controller.getter_MindZone().getter_mindmap().add(sw_label);

				controller.getter_AttributeZone().getter_Mindmap_Label().vector_label_location();
				controller.getter_MindZone().getter_mindmap().revalidate();
				controller.getter_MindZone().getter_mindmap().repaint();

			}
		}
	}

}

class Dialog {
	private JLabel label = new JLabel("");
	private JDialog dialog;

	Dialog(String text, Controller controller) {

		dialog = new JDialog();
		dialog.getContentPane();
		dialog.add(label);

		label.setText(text.toString());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		int x = controller.getter_MindZone().getter_mindmap().getX();
		int y = controller.getter_MindZone().getter_mindmap().getY();
		int w = controller.getter_MindZone().getter_mindmap().getWidth();
		int h = controller.getter_MindZone().getter_mindmap().getHeight();
		dialog.setLocation(x + w / 2, y + h / 2 - 200);
		dialog.setSize(200, 100);
		dialog.setModal(true);
		dialog.setVisible(true);

	}
}

class Mindmap_Label {
	private Controller controller;
	private JLabel node;
	private Point point;
	private Mindmap_Label This = this;
	private JLabel up_label, down_label, left_label, right_label;
	private JLabel ne_label, nw_label, se_label, sw_label;

	Mindmap_Label() {
		first_vector_label();
	}

	Mindmap_Label(Controller controller) {
		this.controller = controller;
	}

	Mindmap_Label(String name, Controller controller) {
		this.controller = controller;
		first_vector_label();

		node = new JLabel(name);
		node.setHorizontalAlignment(SwingConstants.CENTER);
		node.setBackground(Color.WHITE);
		node.setOpaque(true);
		node.addMouseListener(new Mindmap_Label_Listener());
		node.addMouseMotionListener(new Mindmap_Label_Listener());

		up_label.addMouseMotionListener(new Vector_Label_Listener());
		down_label.addMouseMotionListener(new Vector_Label_Listener());
		left_label.addMouseMotionListener(new Vector_Label_Listener());
		right_label.addMouseMotionListener(new Vector_Label_Listener());
		ne_label.addMouseMotionListener(new Vector_Label_Listener());
		nw_label.addMouseMotionListener(new Vector_Label_Listener());
		se_label.addMouseMotionListener(new Vector_Label_Listener());
		sw_label.addMouseMotionListener(new Vector_Label_Listener());

		up_label.addMouseListener(new Vector_Label_Listener());
		down_label.addMouseListener(new Vector_Label_Listener());
		left_label.addMouseListener(new Vector_Label_Listener());
		right_label.addMouseListener(new Vector_Label_Listener());
		ne_label.addMouseListener(new Vector_Label_Listener());
		nw_label.addMouseListener(new Vector_Label_Listener());
		se_label.addMouseListener(new Vector_Label_Listener());
		sw_label.addMouseListener(new Vector_Label_Listener());

	}

	public String toString() {
		return node.getText();
	}

	void setter_controller(Controller controller) {
		this.controller = controller;
	}

	JLabel getter_up_label() {
		return up_label;
	}

	JLabel getter_down_label() {
		return down_label;
	}

	JLabel getter_left_label() {
		return left_label;
	}

	JLabel getter_right_label() {
		return right_label;
	}

	JLabel getter_ne_label() {
		return ne_label;
	}

	JLabel getter_nw_label() {
		return nw_label;
	}

	JLabel getter_se_label() {
		return se_label;
	}

	JLabel getter_sw_label() {
		return sw_label;
	}

	JLabel getter_node() {
		return node;
	}

	void setter_node(int x, int y, int w, int h, Color c) {
		Color back_color = c;
		int color_R = back_color.getRed();
		int color_G = back_color.getGreen();
		int color_B = back_color.getBlue();
		Color reverse_color = new Color(255 - color_R, 255 - color_G, 255 - color_B);
		node.setLocation(x, y);
		node.setSize(new Dimension(w, h));
		node.setPreferredSize(new Dimension(w, h));
		node.setFont(new Font(null, Font.BOLD, node.getHeight() / 2));

		node.setBackground(back_color);
		if (color_R == color_G && color_R == color_B && color_R < 128)
			node.setForeground(Color.white);
		else if (color_R == color_G && color_R == color_B && color_R >= 128)
			node.setForeground(Color.black);
		else
			node.setForeground(reverse_color);

	}

	void setter_node(int x, int y, int w, int h) {
		node.setLocation(x, y);
		node.setSize(new Dimension(w, h));
		node.setPreferredSize(new Dimension(w, h));
		node.setFont(new Font(null, Font.BOLD, node.getHeight() / 2));

		node.setToolTipText("클릭으로 속성을 변경합니다");
	}

	void first_vector_label() {
		up_label = new JLabel();
		up_label.setHorizontalAlignment(SwingConstants.CENTER);
		up_label.setBackground(Color.BLACK);
		up_label.setOpaque(true);
		up_label.setToolTipText("세로(위) 길이를 증가시킵니다.");

		down_label = new JLabel();
		down_label.setHorizontalAlignment(SwingConstants.CENTER);
		down_label.setBackground(Color.BLACK);
		down_label.setOpaque(true);
		down_label.setToolTipText("세로(아래) 길이를 증가시킵니다.");

		left_label = new JLabel();
		left_label.setHorizontalAlignment(SwingConstants.CENTER);
		left_label.setBackground(Color.BLACK);
		left_label.setOpaque(true);
		left_label.setToolTipText("가로(왼쪽) 길이를 증가시킵니다.");

		right_label = new JLabel();
		right_label.setHorizontalAlignment(SwingConstants.CENTER);
		right_label.setBackground(Color.BLACK);
		right_label.setOpaque(true);
		right_label.setToolTipText("가로(오른쪽) 길이를 증가시킵니다.");

		ne_label = new JLabel();
		ne_label.setHorizontalAlignment(SwingConstants.CENTER);
		ne_label.setBackground(Color.BLACK);
		ne_label.setOpaque(true);
		ne_label.setToolTipText("대각선(NE) 길이를 증가시킵니다.");

		nw_label = new JLabel();
		nw_label.setHorizontalAlignment(SwingConstants.CENTER);
		nw_label.setBackground(Color.BLACK);
		nw_label.setOpaque(true);
		nw_label.setToolTipText("대각선(NW) 길이를 증가시킵니다.");

		se_label = new JLabel();
		se_label.setHorizontalAlignment(SwingConstants.CENTER);
		se_label.setBackground(Color.BLACK);
		se_label.setOpaque(true);
		se_label.setToolTipText("대각선(SE) 길이를 증가시킵니다.");

		sw_label = new JLabel();
		sw_label.setHorizontalAlignment(SwingConstants.CENTER);
		sw_label.setBackground(Color.BLACK);
		sw_label.setOpaque(true);
		sw_label.setToolTipText("대각선(SW) 길이를 증가시킵니다.");

	}

	void vector_label_location() {
		up_label.setBounds(node.getX(), node.getY() - 5, node.getWidth(), 5);
		down_label.setBounds(node.getX(), node.getY() + node.getHeight(), node.getWidth(), 5);
		left_label.setBounds(node.getX() - 5, node.getY(), 5, node.getHeight());
		right_label.setBounds(node.getX() + node.getWidth(), node.getY(), 5, node.getHeight());

		ne_label.setBounds(node.getX() - 5 + node.getWidth() + 5, node.getY() - 5, 5, 5);
		nw_label.setBounds(node.getX() - 5, node.getY() - 5, 5, 5);
		se_label.setBounds(node.getX() - 5, node.getY() + node.getHeight(), 5, 5);
		sw_label.setBounds(node.getX() + node.getWidth(), node.getY() + node.getHeight(), 5, 5);

	}

	@SuppressWarnings("deprecation")
	class Mindmap_Label_Listener extends MouseAdapter {
		private Color back_color = node.getBackground();
		private int color_R = back_color.getRed();
		private int color_G = back_color.getGreen();
		private int color_B = back_color.getBlue();
		private Color reverse_color;

		public void mouseClicked(MouseEvent e) {
			setting_attribute();

			if (up_label.isShowing() == true) {
				up_label.show(false);
				down_label.show(false);
				left_label.show(false);
				right_label.show(false);
				ne_label.show(false);
				nw_label.show(false);
				se_label.show(false);
				sw_label.show(false);
			} else {
				up_label.show(true);
				down_label.show(true);
				left_label.show(true);
				right_label.show(true);
				ne_label.show(true);
				nw_label.show(true);
				se_label.show(true);
				sw_label.show(true);
			}
			controller.getter_MindZone().getter_mindmap().add(up_label);
			controller.getter_MindZone().getter_mindmap().add(down_label);
			controller.getter_MindZone().getter_mindmap().add(left_label);
			controller.getter_MindZone().getter_mindmap().add(right_label);
			controller.getter_MindZone().getter_mindmap().add(ne_label);
			controller.getter_MindZone().getter_mindmap().add(nw_label);
			controller.getter_MindZone().getter_mindmap().add(se_label);
			controller.getter_MindZone().getter_mindmap().add(sw_label);

			vector_label_location();
			controller.getter_MindZone().getter_mindmap().revalidate();
			controller.getter_MindZone().getter_mindmap().repaint();
		}

		public void mousePressed(MouseEvent e) {
			point = e.getPoint();
			color_choose();
			setting_attribute();

		}

		public void mouseReleased(MouseEvent e) {
			color_choose();
			setting_attribute();
		}

		public void mouseDragged(MouseEvent e) {
			controller.getter_MindZone_pane().repaint();
			node.setFont(new Font(null, Font.BOLD, node.getHeight() / 2));

			Point ep = e.getPoint();
			Component temp = (JComponent) (e.getSource());
			Point point2 = temp.getLocation();
			temp.setLocation(point2.x + ep.x - point.x, point2.y + ep.y - point.y);
			setting_attribute();

			vector_label_location();
		}

		private void color_choose() {
			this.back_color = node.getBackground();
			this.color_R = back_color.getRed();
			this.color_G = back_color.getGreen();
			this.color_B = back_color.getBlue();
			this.reverse_color = new Color(255 - color_R, 255 - color_G, 255 - color_B);
			node.setBackground(reverse_color);
			if (color_R == color_G && color_R == color_B && color_R < 128)
				node.setForeground(Color.black);
			else if (color_R == color_G && color_R == color_B && color_R >= 128)
				node.setForeground(Color.white);
			else
				node.setForeground(back_color);
		}

	}

	class Vector_Label_Listener extends MouseAdapter {
		private int ep_x, ep_y;
		private Cursor cursor;

		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == up_label) {
				cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
				up_label.setCursor(cursor);

			} else if (e.getSource() == down_label) {
				cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
				down_label.setCursor(cursor);
			} else if (e.getSource() == left_label) {
				cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
				left_label.setCursor(cursor);
			} else if (e.getSource() == right_label) {
				cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
				right_label.setCursor(cursor);
			} else if (e.getSource() == ne_label) {
				cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
				ne_label.setCursor(cursor);
			} else if (e.getSource() == nw_label) {
				cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
				nw_label.setCursor(cursor);
			} else if (e.getSource() == se_label) {
				cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
				se_label.setCursor(cursor);
			} else if (e.getSource() == sw_label) {
				cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
				sw_label.setCursor(cursor);
			}
		}

		public void mousePressed(MouseEvent e) {
			ep_x = e.getX();
			ep_y = e.getY();
		}

		public void mouseDragged(MouseEvent e) {
			node.setFont(new Font(null, Font.BOLD, node.getHeight() / 2));

			if (e.getSource() == up_label) {
				node.setBounds(node.getX(), node.getY() + (e.getY() - ep_y), node.getWidth(),
						node.getHeight() - (e.getY() - ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == down_label) {
				node.setBounds(node.getX(), node.getY(), node.getWidth(), node.getHeight() + (e.getY() - ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == left_label) {
				node.setBounds(node.getX() + (e.getX() - ep_x), node.getY(), node.getWidth() - (e.getX() - ep_x),
						node.getHeight());
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == right_label) {
				node.setBounds(node.getX(), node.getY(), node.getWidth() + (e.getX() - ep_x), node.getHeight());
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == ne_label) {
				node.setBounds(node.getX(), node.getY() + (e.getY() - ep_y), node.getWidth() + (e.getX() - ep_x),
						node.getHeight() - (e.getY() - ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == nw_label) {
				node.setBounds(node.getX() + (e.getX() - ep_x), node.getY() + (e.getY() - ep_y),
						node.getWidth() - (e.getX() - ep_x), node.getHeight() - (e.getY() - ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == se_label) {
				node.setBounds(node.getX() + (e.getX() - ep_x), node.getY(), node.getWidth() - (e.getX() - ep_x),
						node.getHeight() + (e.getY() + ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			} else if (e.getSource() == sw_label) {
				node.setBounds(node.getX(), node.getY(), node.getWidth() + (e.getX() - ep_x),
						node.getHeight() + (e.getY() - ep_y));
				setting_attribute();
				vector_label_location();
				controller.getter_MindZone().getter_mindmap().repaint();
				controller.getter_MindZone().getter_mindmap().revalidate();
			}

		}
	}

	@SuppressWarnings("deprecation")
	void setting_attribute() {
		if (controller.getter_AttributeZone().getter_Mindmap_Label() != null) {
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_up_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_down_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_left_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_right_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_ne_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_nw_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_se_label().show(false);
			controller.getter_AttributeZone().getter_Mindmap_Label().getter_sw_label().show(false);

		}
		controller.getter_AttributeZone().getter_x_var().setText(Integer.toString(node.getX() - 1000));
		controller.getter_AttributeZone().getter_x_var()
				.setFont(controller.getter_AttributeZone().getter_x_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_x_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_x_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().getter_y_var().setText(Integer.toString(node.getY() - 1000));
		controller.getter_AttributeZone().getter_y_var()
				.setFont(controller.getter_AttributeZone().getter_y_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_y_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_y_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().getter_h_var().setText(Integer.toString(node.getHeight()));
		controller.getter_AttributeZone().getter_h_var()
				.setFont(controller.getter_AttributeZone().getter_h_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_h_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_h_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().getter_w_var().setText(Integer.toString(node.getWidth()));
		controller.getter_AttributeZone().getter_w_var()
				.setFont(controller.getter_AttributeZone().getter_w_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_w_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_w_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().getter_node_var().setText(node.getText());
		controller.getter_AttributeZone().getter_node_var()
				.setFont(controller.getter_AttributeZone().getter_node_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_node_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_node_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().getter_color_var()
				.setText(Integer.toHexString(node.getBackground().getRGB()).substring(2, 8));
		controller.getter_AttributeZone().getter_color_var()
				.setFont(controller.getter_AttributeZone().getter_color_var().getFont().deriveFont(24.0f));
		controller.getter_AttributeZone().getter_color_var().setFont(new Font(null, Font.BOLD, 30));
		controller.getter_AttributeZone().getter_color_var().setHorizontalAlignment(SwingConstants.CENTER);

		controller.getter_AttributeZone().setter_Mindmap_Label(This);
	}
}