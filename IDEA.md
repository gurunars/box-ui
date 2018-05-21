# Contextual action registration

This is a brilliant idea of how to register contextual actions to item types.

For each item type (check via is instance of) a contextual action can be either added or removed.

The actions can have several important attributes:

1. isEnabled: (selectedItems: Set<T>) -> Boolean
   determine if the action is applicable to the current subset of items
2. icon: Icon
3. trigger: (selectedItems: Set<T>) -> Unit
   what to do when the action is invoked
   
Triggering any action should close the contextual menu

## Question

How to semantically control the position of the action trigger within the layout?

Which logical quality can be used to position itself around the FAB?

"Importance"? E.g. the more important the action is - the closer it is to the FAB.

The actions should be groupable. If they are related - they should be somehow tied together in
the UI.

This in general a good concept. To have a semantic autolayout view. That can position its items
around the most relevant place based on importance.