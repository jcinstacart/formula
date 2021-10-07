package com.instacart.formula

import com.instacart.formula.internal.DelegateTransitionContext
import com.instacart.formula.internal.combine
import com.instacart.formula.internal.toResult

/**
 * Transition context provides the current [state] and utilities to help
 * create [Transition.Result] within [Transition.toResult].
 *
 * TODO: add Formula.Input as well.
 */
interface TransitionContext<State> {

    val state: State

    /**
     * Returns a result that indicates to do nothing as part of this event.
     */
    fun none(): Transition.Result<Nothing> {
        return Transition.Result.None
    }

    /**
     * Returns a result that contains a new [State] object and optional [effects][Effects]
     * that will be executed after the state is updated.
     */
    fun <State> transition(
        state: State,
        effects: Effects? = null
    ): Transition.Result.Stateful<State> {
        return Transition.Result.Stateful(state, effects)
    }

    /**
     * Returns a result that requests [effects][Effects] to be executed.
     */
    fun transition(
        effects: Effects
    ): Transition.Result.OnlyEffects {
        return Transition.Result.OnlyEffects(effects)
    }

    /**
     * Delegates to another [Transition] to provide the result.
     */
    fun <Event> delegate(transition: Transition<State, Event>, event: Event): Transition.Result<State> {
        return transition.run { toResult(event) }
    }

    /**
     * Delegates to another [Transition] that has [Unit] event type to provide the result.
     */
    fun delegate(transition: Transition<State, Unit>): Transition.Result<State> {
        return delegate(transition, Unit)
    }

    /**
     * Function used to chain multiple transitions together.
     */
    fun <Event> Transition.Result<State>.andThen(
        transition: Transition<State, Event>,
        event: Event
    ): Transition.Result<State> {
        return when (this) {
            Transition.Result.None -> {
                transition.toResult(this@TransitionContext, event)
            }
            is Transition.Result.OnlyEffects -> {
                combine(this, transition.toResult(this@TransitionContext, event))
            }
            is Transition.Result.Stateful -> {
                combine(this, transition.toResult(DelegateTransitionContext(this.state), event))
            }
        }
    }

    /**
     * Function used to chain multiple transitions together.
     */
    fun Transition.Result<State>.andThen(
        transition: Transition<State, Unit>,
    ): Transition.Result<State> {
        return andThen(transition, Unit)
    }

    /**
     * Function to chain updated state with another transition.
     */
    fun <Event> State.andThen(
        transition: Transition<State, Event>,
        event: Event
    ): Transition.Result<State> {
        return transition(this).andThen(transition, event)
    }

    /**
     * Function to chain updated state with another transition.
     */
    fun State.andThen(transition: Transition<State, Unit>): Transition.Result<State> {
        return transition(this).andThen(transition)
    }
}

