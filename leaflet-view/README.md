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
        compile ('com.gurunars.leaflet_view:leaflet-view:0.+@aar') {
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
    public View render(Context context, PageTransitionObservable transitionObservable) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.page_view, null);
        TextView textView = ButterKnife.findById(layout, R.id.pageTitle);
        textView.setText(title);
        return layout;
    }

    @Override
    public String toString() {
        return title;
    }

}

```

Note, the render method gets a **transitionObservable** parameter that can
be use to subscribe to **enter** and **leave** events for individual pages.
The listener is meant to control the state of individual page views returned
by the method. For the notifications of external components use **LeafletView**
level ability to configure a transition listener (see below).

Afterwards add the following to your layouts:

```xml

<com.gurunars.leaflet_view.LeafletView
    android:id="@+id/leafletView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>

```

Configure renderer for the cases when there are no pages:

```java

LeafletView leafletView = (LeafletView) findViewById(R.id.leafletView);

leafletView.setNoPage(new NoPage() {
    @Override
    public View render(Context context, PageTransitionObservable transitionObservable) {
        transitionObservable.addOnEnterListener(new Runnable() {
            @Override
            public void run() {
                setTitle(R.string.empty);
            }
        });
        return LayoutInflater.from(context).inflate(R.layout.no_page_view, null);
    }
});

```

And set page transition listener for the components that are external with
respect to the page:

```java

leafletView.setPageTransitionListener(new PageTransitionListener<TitledPage>() {
    @Override
    public void onEnter(TitledPage page) {
        setTitle(page.getTitle());
    }

    @Override
    public void onLeave(TitledPage page) {
        setTitle(null);
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
