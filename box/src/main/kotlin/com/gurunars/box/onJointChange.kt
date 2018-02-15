package com.gurunars.box

/**
 * Combines changes from several boxes together
 * and invokes a consumer with the combined set
 * of box payloads.
 */
fun onChange(vararg params: IRoBox<*>, hot: Boolean = true, listener: () -> Unit) {
    params.forEach { it.onChange(hot) { _ -> listener() } }
}

/**
 * Combines changes from several boxes together
 * and returns a boxed value that is a transformation result
 * of the combined box payloads.
 */
fun <One, Two, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    transform: (one: One, two: Two) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two) {
        set(transform(one.get(), two.get()))
    }
}

/** same as two arg */
fun <One, Two, Three, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    three: IRoBox<Three>,
    transform: (one: One, two: Two, three: Three) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two, three) {
        set(transform(one.get(), two.get(), three.get()))
    }
}

/** same as two arg */
fun <One, Two, Three, Four, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    three: IRoBox<Three>,
    four: IRoBox<Four>,
    transform: (one: One, two: Two, three: Three, four: Four) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two, three, four) {
        set(transform(one.get(), two.get(), three.get(), four.get()))
    }
}

/** same as two arg */
fun <One, Two, Three, Four, Five, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    three: IRoBox<Three>,
    four: IRoBox<Four>,
    five: IRoBox<Five>,
    transform: (one: One, two: Two, three: Three, four: Four, five: Five) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two, three, four, five) {
        set(transform(one.get(), two.get(), three.get(), four.get(), five.get()))
    }
}

/** same as two arg */
fun <One, Two, Three, Four, Five, Six, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    three: IRoBox<Three>,
    four: IRoBox<Four>,
    five: IRoBox<Five>,
    six: IRoBox<Six>,
    transform: (one: One, two: Two, three: Three, four: Four, five: Five, six: Six) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two, three, four, five, six) {
        set(transform(one.get(), two.get(), three.get(), four.get(), five.get(), six.get()))
    }
}

/** same as two arg */
fun <One, Two, Three, Four, Five, Six, Seven, R> R.merge(
    one: IRoBox<One>,
    two: IRoBox<Two>,
    three: IRoBox<Three>,
    four: IRoBox<Four>,
    five: IRoBox<Five>,
    six: IRoBox<Six>,
    seven: IRoBox<Seven>,
    transform: (one: One, two: Two, three: Three, four: Four, five: Five, six: Six, seven: Seven) -> R
): IRoBox<R> = Box(this).apply {
    onChange(one, two, three, four, five, six, seven) {
        set(transform(one.get(), two.get(), three.get(), four.get(), five.get(), six.get(), seven.get()))
    }
}