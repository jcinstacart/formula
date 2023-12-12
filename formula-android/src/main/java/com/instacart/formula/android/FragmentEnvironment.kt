package com.instacart.formula.android

import android.view.LayoutInflater
import android.view.ViewGroup

data class FragmentEnvironment(
    val logger: (String) -> Unit = {},
    val onScreenError: (FragmentKey, Throwable) -> Unit = { _, it -> throw it },
    val fragmentDelegate: FragmentDelegate = FragmentDelegate(),
) {

    /**
     * Introspection API to track various formula fragment events and their performance.
     */
    open class FragmentDelegate {

        /**
         * Called when new instance of [FormulaFragment] is created.
         */
        open fun onNewInstance(
            fragmentId: FragmentId
        ) = Unit

        /**
         * Instantiates the feature.
         */
        open fun <DependenciesT, KeyT: FragmentKey> initializeFeature(
            fragmentId: FragmentId,
            factory: FeatureFactory<DependenciesT, KeyT>,
            dependencies: DependenciesT,
            key: KeyT,
        ): Feature<*> {
            return factory.initialize(dependencies, key)
        }

        /**
         * Called from [FormulaFragment.onCreateView] to instantiate the view.
         */
        open fun createView(
            fragmentId: FragmentId,
            viewFactory: ViewFactory<Any>,
            inflater: LayoutInflater,
            container: ViewGroup?,
        ): FeatureView<Any> {
            return viewFactory.create(inflater, container)
        }

        /**
         * Called when we are ready to apply [output] to the view.
         */
        open fun setOutput(fragmentId: FragmentId, output: Any, applyOutputToView: (Any) -> Unit) {
            applyOutputToView(output)
        }
    }
}
