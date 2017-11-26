package com.gurunars.crud_item_list

import com.gurunars.android_utils.Icon
import com.gurunars.databinding.Box
import com.gurunars.item_list.SelectableItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestDescriptor : ItemTypeDescriptor<StringItem> {
    override val icon: Icon
        get() = TODO("NOT NEEDED FOR TESTS")

    override fun bindRow(field: Box<SelectableItem<StringItem>>)
        = TODO("NOT NEEDED FOR TESTS")

    override fun validate(item: StringItem)
        = TODO("NOT NEEDED FOR TESTS")

    override fun createNewItem()
        = StringItem("NEW")

    override fun bindForm(field: Box<StringItem>)
        = TODO("NOT NEEDED FOR TESTS")

    enum class Type {
        ONE, TWO
    }

}

class StateManagerTest {

    private lateinit var manager: StateMachine<StringItem>

    @Before
    fun before() {
        manager = StateMachine<StringItem>(
            {},
            mapOf(
                TestDescriptor.Type.ONE to TestDescriptor(),
                TestDescriptor.Type.TWO to TestDescriptor()
            ),
            { supplier, consumer -> consumer(supplier()) }
        )
    }

    private fun checkState(expectedState: State<StringItem>)
        = assertEquals(
        expectedState,
        manager.state.get()
    )

    @Test
    fun plainOpenForOneItemType_leadsToFormState() {
        manager = StateMachine<StringItem>(
            {},
            mapOf(TestDescriptor.Type.ONE to TestDescriptor()),
            { supplier, consumer -> consumer(supplier()) }
        )
        manager.isOpen.set(true)
        checkState(State(
            itemInEdit = StringItem("NEW")
        ))
    }

    @Test
    fun plainOpen_leadsToCreationState() {
        manager.isOpen.set(true)
        checkState(State(isCreationMode = true))
    }

    @Test
    fun openExplicitContextual_setsRespectiveFlag() {
        manager.openExplicitContextualMenu()
        checkState(State(explicitContextual = true))
    }

    @Test
    fun selectingItems_addsThemToSelection() {
        val selection = setOf("One", "Two", "Three").itemize()
        manager.selectedItems.set(selection)
        checkState(State(selectedItems = selection))
    }

    @Test
    fun unselectingItems_removesThemFromSelection() {
        val selection = setOf<StringItem>()
        manager.selectedItems.set(selection)
        checkState(State(selectedItems = selection))
    }

    @Test
    fun loadingItemByType_leadsToAsyncCreationOfTheItem() {
        manager.loadType(TestDescriptor.Type.ONE)
        checkState(State(itemInEdit = StringItem("NEW")))
    }

    @Test
    fun openingMenuWithTooSlowFormLoader_doesNotCauseRecursion() {
        manager = StateMachine<StringItem>(
            {},
            mapOf(TestDescriptor.Type.ONE to TestDescriptor()),
            { supplier, consumer -> }
        )
        manager.isOpen.set(true)
        checkState(State(
            itemTypeInLoad = TestDescriptor.Type.ONE
        ))
    }

    @Test
    fun loadingItemAsIs_leadsToFormState() {
        manager.loadItem(StringItem("NEW"))
        checkState(State(itemInEdit = StringItem("NEW")))
    }

    @Test
    fun closingTheMenu_nullifiesTheStateButDoesNotChangeViewMode() {
        manager.selectedItems.set(setOf("One", "Two", "Three").itemize())
        manager.state.set(
            State(
                isCreationMode = true,
                explicitContextual = true,
                itemInEdit = StringItem("One"),
                selectedItems = setOf("One", "Two", "Three").itemize(),
                itemTypeInLoad = TestDescriptor.Type.ONE
            )
        )
        manager.isOpen.set(false)
        checkState(State())
        assertEquals(ViewMode.FORM, manager.viewMode.get())
    }

}

class StateTest {

    private fun checkState(state: State<StringItem>, viewMode: ViewMode, isOpen: Boolean) {
        assertEquals(
            viewMode,
            state.viewMode
        )
        assertEquals(isOpen, state.isOpen)
    }

    @Test
    fun allActive_isForm() {
        checkState(
            State(
                isCreationMode = true,
                explicitContextual = true,
                itemInEdit = StringItem("One"),
                selectedItems = setOf("One", "Two", "Three").itemize(),
                itemTypeInLoad = TestDescriptor.Type.ONE
            ),
            ViewMode.FORM,
            true
        )
    }

    @Test
    fun itemInEdit_isForm() {
        checkState(
            State(
                itemInEdit = StringItem("One")
            ),
            ViewMode.FORM,
            true
        )
    }

    @Test
    fun itemTypeInLoad_isLoading() {
        checkState(
            State(
                itemTypeInLoad = TestDescriptor.Type.ONE
            ),
            ViewMode.LOADING,
            true
        )
    }

    @Test
    fun explicitContextual_isContenxtual() {
        checkState(
            State(
                explicitContextual = true
            ),
            ViewMode.CONTEXTUAL,
            true
        )
    }

    @Test
    fun selectedItems_isContenxtual() {
        checkState(
            State(
                selectedItems = setOf("One", "Two", "Three").itemize()
            ),
            ViewMode.CONTEXTUAL,
            true
        )
    }

    @Test
    fun creationMode_isCreation() {
        checkState(
            State(
                isCreationMode = true
            ),
            ViewMode.CREATION,
            true
        )
    }

    @Test
    fun default_isEmpty() {
        checkState(State(), ViewMode.EMPTY, false)
    }

}