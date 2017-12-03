package com.gurunars.item_list

import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy

internal fun getKryo() = Kryo().apply {
    instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
}