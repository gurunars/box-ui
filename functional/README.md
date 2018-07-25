# Functional UI

Use: https://github.com/facebook/yoga

## Usage

```kotlin
fun ViewBuilder.carComponent(listOfCars: List<Car>) = view {
    margin=Bounds(top==11.dp, left=12.dp, bottom=13.mm, right=16.mm)
    padding=Bounds(top==11.dp, left=12.dp, bottom=13.mm, right=16.mm)

    textView {
        textColor=Color("#FFBBAA")
        text=R.string.foobar.localized
    }
    
    textView {
        textColor=Color.RED
        text=R.string.zooloo.localized
    }
    
    view {
        padding=Bounds(all=20.dp)
        textView(R.string.clickable.localized)
        // TODO: design good looking click interaction
    }
    
    listView(listOfCars) {
        view {
            textView("Label")
            textView(it.name)
            textView("Model")
            textView(it.model)
        }
    }
    
}

fun ViewBuilder.metaCarComponent(
    listOfCars: List<Car>
) = view {
    
    if (listOfCars.isEmpty()) {
        view {
            padding=Bounds(all=15.dp)
            textView(R.string.empty.localized)
        }
    } else {
        carComponent(listOfCars)
    }
    
    style {
        when(it) {
            is TextView -> {
                if (it.textSize > 10.sp) {
                    it.copy(textColor=it.textColor ?: Color.BLUE)
                } else {
                    it.copy(textColor=it.textColor ?: Color.YELLOW)             
                }
            }
            else -> it
        }
    }

}


```

## Widgets

### CalendarView

