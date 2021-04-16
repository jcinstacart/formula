package com.instacart.formula.android

import com.instacart.formula.fragment.FormulaFragment
import com.instacart.formula.fragment.FragmentKey
import com.instacart.formula.fragment.FragmentFlowStore
import com.instacart.formula.integration.FragmentBindingBuilder

/**
 * Feature factory is responsible for creating a [Feature] for a specific [fragment key][Key].
 *
 * ```
 * // An example from samples/todoapp
 * class TaskListFeatureFactory : FeatureFactory<TaskListFeatureFactory.Dependencies, TaskListKey>() {
 *
 *   interface Dependencies {
 *     fun taskRepo(): TaskRepo
 *     fun taskListInput(): TaskListFormula.Input
 *   }
 *
 *   override fun initialize(dependencies: Dependencies, key: TaskListKey): Feature<*> {
 *     // Note: we could create our own internal dagger component here using the dependencies.
 *     val formula = TaskListFormula(dependencies.taskRepo())
 *     return Feature(
 *       state = formula.toObservable(dependencies.taskListInput()),
 *       viewFactory = ViewFactory.fromLayout(R.layout.task_list) { view ->
 *         val renderView = TaskListRenderView(view)
 *         featureView(renderView)
 *       }
 *     )
 *   }
 * }
 * ```
 *
 * Once we define a [FeatureFactory], we need to [bind][FragmentBindingBuilder.bind] it to a
 * [FragmentFlowStore]. The fragment flow store will call [initialize] the first time
 * [FormulaFragment] with a new [Key] is attached. It will subscribe to the state management
 * and persist it across configuration changes.
 *
 * @param Dependencies dependencies to instantiate this feature. Usually defined by an interface.
 * @param Key a type of fragment key that is used to identify this feature.
 *
 */
interface FeatureFactory<in Dependencies, in Key : FragmentKey> {

    /**
     * Initializes state observable and a view factory for a specific [key].
     */
    fun initialize(dependencies: Dependencies, key: Key): Feature<*>
}