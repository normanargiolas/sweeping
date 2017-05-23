package it.namron.sweeping.activity;

public class NavDrawerItem {
	private String title;
	private int icon;
	private int id;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}