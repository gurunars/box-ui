# Float Menu

FAB based menu.

## Show cases

The menu is opened with multiple animations via a button click:

<img src="showcases/open_close.gif" width="320">

The menu can be toggled externally:

<img src="showcases/toggle_menu.gif" width="320">

There is a support for both left and right handed people:

<img src="showcases/left_and_right.gif" width="320">

The menu has two modes: with and without a background layover. With it the all
the clicks in semi-transparent gray area are blocked. Without it any clicks are
passed to the layer below the menu.

<img src="showcases/background_toggle.gif" width="320">

## Installation

Add the following to your app's **build.gradle**:

    dependencies {
        ...
        compile ('com.gurunars.floatmenu:core:0.+@aar') {
            transitive = true
        }
        ...
    }

## Usage

First, add a reference in your layout files:

```xml

    <com.gurunars.floatmenu.FloatMenu
        android:id="@+id/floatingMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Second, set menu open/close event listener:

```java

    import com.gurunars.floatmenu.AnimationListener;
    import com.gurunars.floatmenu.FloatMenu;

    ...

    FloatMenu floatingMenu = (FloatMenu) findViewById(R.id.floatingMenu);

    floatingMenu.setOnCloseListener(new AnimationListener() {
        @Override
        public void onStart(int projectedDuration) {

        }

        @Override
        public void onFinish() {

        }
    });

    floatingMenu.setOnOpenListener(new AnimationListener() {
        @Override
        public void onStart(int projectedDuration) {

        }

        @Override
        public void onFinish() {

        }
    });

```

## Customization

The menu has a bunch of custom attributes aiming to tweak its behavior and
look&feel.

### hasOverlay

Menu supports two modes: with and without overlay. When the overlay is
disabled all touch events in the blank space are propagated to the parent.

Defaults to True.

To make arbitrary ViewGroups intercept click mark them as clickable.

**XML snippet**

```xml

    <com.gurunars.floatmenu.FloatMenu
        app:fabHasOverlay="false"
        ... />

```

**Java snippet**

```java

    floatingMenu.setHasOverlay(false);

```

### leftHanded

Make it easier for both left and right handed people to use the menu.

If True - FAB shall be in the bottom left corner.
If False - in the bottom right.

**XML snippet**

```xml

    <com.gurunars.floatmenu.FloatMenu
        app:fabLeftHanded="true"
        ... />

```

**Java snippet**

```java

    floatingMenu.setLeftHanded(true);

```

### rotationDuration

Time in milliseconds that it should take for the menu to get opened/closed
after the button is clicked.

**XML snippet**

```xml

    <com.gurunars.floatmenu.FloatMenu
        app:fabRotationDuration="500"
        ... />

```

**Java snippet**

```java

    floatingMenu.setRotationDuration(500);

```

### FAB icon customiation

* **fabOpenIcon** - Icon shown in the button clicking which opens the menu.
* **fabOpenIconBgColor** - Background color of the button clicking which opens the menu.
* **fabOpenIconFgColor** - Color of the icon shown in the button clicking which opens the menu.
* **fabCloseIcon** - Icon shown in the button clicking which closes the menu.
* **fabCloseIconBgColor** - Background color of the button clicking which closes the menu.
* **fabCloseIconFgColor** - Color of the icon shown in the button clicking which closes the menu.

**XML snippet**

```xml

    <com.gurunars.floatmenu.FloatMenu
        app:fabOpenIcon="@drawable/Monkey"
        app:fabOpenIconBgColor="@color/Black"
        app:fabOpenIconFgColor="@color/White"
        app:fabCloseIcon="@drawable/Wolf"
        app:fabCloseIconBgColor="@color/White"
        app:fabCloseIconFgColor="@color/Black"
        ... />

```

**Java snippet**

```java

    floatingMenu.setOpenIcon(R.drawable.Monkey);
    floatingMenu.setOpenIconBgColor(context.getColor(R.color.Black));
    floatingMenu.setOpenIconFgColor(context.getColor(R.color.White));

    floatingMenu.setCloseIcon(R.drawable.Wolf);
    floatingMenu.setCloseIconBgColor(context.getColor(R.color.White));
    floatingMenu.setCloseIconFgColor(context.getColor(R.color.Black));
```

### menuView

View to be shown within the menu pane.

**Java snippet**

```java

    floatingMenu.setMenuView(inflate(this, R.layout.menu_view, null));

```

### contentView

View to be shown behind the menu pane.

**Java snippet**

```java

    floatingMenu.setContentView(inflate(this, R.layout.content_view, null));

```
