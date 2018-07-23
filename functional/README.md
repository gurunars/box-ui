# Functional UI

Use: https://github.com/facebook/yoga

## Usage

```kotlin
fun render(listOfCars: List<Car>) = view {
    margin=Rect(top==11.dp, left=12.dp, bottom=13.mm, right=16.mm)
    padding=Rect(top==11.dp, left=12.dp, bottom=13.mm, right=16.mm)

    textView {
        textColor=Color("#FFBBAA")
        text=Text("FooBar")
    }
    
    textView {
        textColor=Color.RED
        text=Text(R.string.foobar)
    }
    
    view {
        padding=Rect(all=20.dp)
        textView {
            text=Text(R.string.clickable)
        }
        // TODO: design good looking click interaction
    }
    
    listView(listOfCars) {
        textView {
            
        }
    }
}


```

## Widgets

### CalendarView

