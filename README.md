# Android CRUD UI

A collection of modules aiming to simplify creation of very specific simple
CRUD apps for Android that require several capabilities:

- creating and editing items via individual item type specific forms
- manipulating the lists of items via a set of controls
- customizing list item rendering by associating types with type specific
  renderers
- managing look and feel of the creation menu

The project is split into multiple reusable modules that can be employed on
their own.

- [item-list](item-list/README.md) - a component based on RecyclerView with
  a dramatically simplified interface capable of detecting payload changes in
  the collection items and triggering proper animations based on those changes.
  Apart from the animations the component handles custom empty pages.
- [floatmenu](floatmenu/README.md) - a component for creating FAB based menus
  to manipulate the app. In contrast with the toolbar it aims to simplify the
  way the app can be managed with left or right hand thumb on a large phone
  screen.
- [leaflet-view](leaflet-view/README.md) - a component based on ViewPager with
  a dramatically simplified interface. Apart from simpler interfaces it also
  handles empty pages.
- [android-utils](android-utils/README.md) - a set of helpers for beautifying
  the UI - such as a generic way to supply shadows to views with shape drawable
  backgrounds.
- [crud-item-list](crud-item-list/README.md) - one more layer of abstraction
  above the item-list component. It contains the logic and UI controls to
  manipulate individual items in the list. I.e. the UI allows to select, move,
  edit, remove and create the items out of the box.

NOTE: the project has a very opinionated set of constraints that guarantees
that simple CRUD apps can be created with as little cognitive effort as
possible.

## More information

Check the apps in **examples** folder for a reference.

Check [javadoc](https://gurunars.github.io/android-crud-ui/).