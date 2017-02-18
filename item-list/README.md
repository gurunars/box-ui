# Item List

A RecyclerView based widget to simplify the item list changes' animations.

The way it differes from plain RecyclerView is by exposing only a single
interface method to manipulate the payload - **setItems**. All animations
and adapter management is done inside the view.

<img src="showcase.gif" width="320">

## Usage

Add the following to your app's **build.gradle**:

    dependencies {
        ...
        compile ('com.gurunars.item_list:item-list:0.+@aar') {
            transitive = true
        }
        ...
    }

Create a subclass of the Item:

```java
import com.gurunars.item_list.Item;

public class AnimalItem implements Item {

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    private Type type;
    private long id;
    private int version;

    public void update() {
        this.version++;
    }

    public AnimalItem(long id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public Enum getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof AnimalItem)) {
            return false;
        }
        AnimalItem other = (AnimalItem) obj;
        return id == other.id && type == other.type && version == other.version;
    }

    @Override
    public String toString() {
        return "" + id + " @ " + version;
    }
}

```

Put the following into your layout file:

```xml

<com.gurunars.item_list.ItemList
    android:id="@+id/itemList"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

```

Implement the renderer:

```java

class AnimalBinder implements ItemViewBinder<AnimalItem> {

    @Override
    public View getView(Context context) {
        TextView text = new TextView(context);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding);
        text.setPadding(padding, padding, padding, padding);
        return text;
    }

    private void animateUpdate(final View view) {
        view.clearAnimation();
        ValueAnimator anim = new ValueAnimator();
        anim.setFloatValues((float) 1.0, (float) 0.0, (float) 1.0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        anim.setDuration(1300);
        anim.start();
    }

    @Override
    public void bind(View itemView, AnimalItem item, @Nullable AnimalItem previousItem) {
        ((TextView) itemView).setText(item.toString() + " [" +
                item.getType().name().toLowerCase() + "]");
        if (previousItem != null) {
            animateUpdate(itemView);  // make the view flash once on update
        }
    }
}

```

If you don't implement the renderer - the default one shall be used - a plain
text view showing the string returned by a **toString()** method of the Item
instance.

Note, bind method gets an item to render now and a previous version of the
item. I.e. the item with the same id but with a different payload. It can be
null in case if the item did not exists before - i.e. if it just was created.

Implement the empty page renderer:

```java

class EmptyBinder implements EmptyViewBinder {

    @Override
    public View getView(Context context) {
        TextView view = new TextView(context);
        view.setGravity(Gravity.CENTER);
        view.setText(R.string.empty);
        return view;
    }

}

```

In your component place the following configs:

```java

// obtain the view from a layout

ItemList<TestItem> itemList = (ItemList<TestItem>) findViewById(R.id.itemList);

// set renderer for each type

itemList.registerItemViewBinder(AnimalItem.Type.LION, new AnimalBinder());
itemList.registerItemViewBinder(AnimalItem.Type.MONKEY, new AnimalBinder());
itemList.registerItemViewBinder(AnimalItem.Type.TIGER, new AnimalBinder());
itemList.registerItemViewBinder(AnimalItem.Type.WOLF, new AnimalBinder());

// set empty page rebderer

itemList.setEmptyViewBinder(new EmptyBinder());

```
And finally set the collection to a specific set of items:

```java

itemList.setItems(Arrays.asList(
    new AnimalItem(0, AnimalItem.Type.LION),
    new AnimalItem(1, AnimalItem.Type.MONKEY),
    new AnimalItem(2, AnimalItem.Type.TIGER),
    new AnimalItem(3, AnimalItem.Type.WOLF)
));

```

Note, the widget retains the collection of items in its internal state thus
whenever you invoke setItems method again a new list is diffed against
the old one and proper animations are invoked visualizing the changes.
