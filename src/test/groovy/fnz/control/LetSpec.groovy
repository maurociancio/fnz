package fnz.control

import static fnz.data.Fn.val
import static fnz.data.Fn.Just
import static fnz.control.Let.let
import static fnz.control.Unless.unless
import static fnz.control.Where.check

import fnz.data.Maybe
import spock.lang.Unroll
import spock.lang.Specification

class LetSpec extends Specification {

    def 'Simple let expression'() {
        when: 'Initializing expression and executing closure'
            // tag::simpleLet[]
            Maybe<Integer> result = let(x: 10, y: 20) {
                return Just(x + y)
            }
            // end::simpleLet[]
        then: 'There should return a value'
            result.isPresent() == true
        and: 'the value should be...'
            val(result) == 30
    }

    // tag::nestedLet[]
    def 'Nesting lets'() {
        when: 'Initializing expression and executing closure'
            Maybe<Integer> result = let(x: 10, y: 20) {
                def z = x + y
                let(x:6, y: 4) {
                    return Just(x + y + z)
                }
            }
        then: 'There should be a value'
            result.isPresent()
        and: 'The value should be built from the deeper values'
            val(result) == 40
    }
    // end::nestedLet[]

    def 'Evaluating let expressions'() {
        when: 'Initializing expression and executing closure'
            // tag::computedValues[]
            Maybe<Integer> result =
                let(x: { 10 }, y: { 20 }, z: { x + y }) {
                    return Just(z + 1)
                }
            // end::computedValues[]
        then: 'There should return a value'
            result.isPresent() == true
        and: 'the value should be...'
            val(result) == 31
    }

    def 'Using unless within let'() {
        when: "Using less inside a let expression"
            Maybe<Integer> result =
                let(x: 10, y: 5, z: unk) {
                    unless((x + y + z ) == 20) {
                       return Just(z)
                    }
                }
        then: "The option result should be the expected"
            result.isPresent() == isValid
        where: "The possible unknown values are"
            unk | isValid
            5   | false
            4   | true
            0   | true
    }

    def 'Using where within let'() {
        when: 'Having a where clause within a let'
            Maybe<Integer> result =
                let(x: { xparam }, y: {yparam}, z: { x + y }) {
                    check(measure: z) {
                        when { measure <= MINIMUM } then { MINIMUM }
                        when { measure <= MEDIUM }  then { MEDIUM }
                        when { measure <= MAXIMUM } then { MAXIMUM }
                        where {
                            MINIMUM = 10
                            MEDIUM  = 50
                            MAXIMUM = 100
                        }
                    }
                }
        then: 'The value should be the expected'
            val(result) == expected
        where: 'Possible values are'
            xparam | yparam | expected
            4      | 3      | 10
            20     | 30     | 50
            60     | 30     | 100
    }

}