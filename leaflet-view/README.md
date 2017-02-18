# Leaflet View

A convenient wrapper around ViewPager on steroids

## Show cases

Add pages:

<img src="showcases/create.gif" width="320">

Edit a page:

<img src="showcases/edit.gif" width="320">

Navigate from page to page:

<img src="showcases/goto.gif" width="320">

Delete pages:

<img src="showcases/delete.gif" width="320">

## Usage

Add the following to your app's **build.gradle**:

    dependencies {
        ...
        compile ('com.gurunars.leaflet_view:core:0.+@aar') {
            transitive = true
        }
        ...
    }

Next, define a page subclass that shall be shown in individual leaflet
subviews:

```java

class TitledPage implements Page {

    private long id;
    private String title;

    TitledPage(String title) {
        this.id = System.nanoTime();
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }

}

```

Note, you must impl

Afterwards add the following to your layouts:

```xml

<com.gurunars.leaflet_view.LeafletView
    android:id="@+id/leafletView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>

```

Configure renderers for individual pages and for the cases when there are no
pages:

```java

LeafletView<View, TitledPage> leafletView = (LeafletView<View, TitledPage>) findViewById(R.id.leafletView);

leafletView.setPageRenderer(new PageRenderer<View, TitledPage>() {
    @Override
    public View renderPage(TitledPage page) {
        View viewGroup = LayoutInflater.from(ActivityMain.this).inflate(
                R.layout.page_view, leafletView, false);
        TextView textView = (TextView) viewGroup.findViewById(
                R.id.pageTitle);
        textView.setText(page.toString());
        viewGroup.setTag(page);
        return viewGroup;
    }

    @Override
    public void enter(View pageView) {
        setTitle(pageView.getTag().toString());
    }

    @Override
    public void leave(View pageView) {

    }
});

leafletView.setNoPageRenderer(new NoPageRenderer() {
    @Override
    public View renderNoPage() {
        return LayoutInflater.from(ActivityMain.this).
                inflate(R.layout.no_page_view, leafletView, false);
    }

    @Override
    public void enter() {
        setTitle(R.string.empty);
    }

});

```

Set a collection of pages to a specific list:

```java

TitledPage pageOne = new TitledPage("ONE");
TitledPage pageTwo = new TitledPage("TWO");
leafletView.setPages(Arrays.asList(pageOne, pageTwo));

```

Navigate to a particular page:

```java

leafletView.goTo(pageTwo);

```
