package fnz.ast

import groovy.transform.CompileStatic

import fnz.Fnz
import fnz.data.Try
import fnz.data.Monad
import fnz.data.Either
import fnz.data.Maybe
import fnz.data.Functor
import fnz.data.Function
import fnz.data.Applicative
import fnz.data.ListMonad
import fnz.data.TypeAwareFunction

@CompileStatic
final class FnzExtensionModule {

    static <A> Either.Left<A> Left(Object o, A source) {
        return Fnz.Left(source)
    }

    static <A> Either.Right<A> Right(Object o, A source) {
        return Fnz.Right(source)
    }

    static <A> Either<A> Either(Object o, A source) {
        return Fnz.Either(source)
    }

    static <A> Maybe.Just<A> Just(Object o, A source) {
        return Fnz.Just(source)
    }

    static <A> Maybe.Nothing<A> Nothing(Object o) {
        return Fnz.Nothing()
    }

    static <A> Maybe<A> Maybe(Object o, A source) {
        return Fnz.Maybe(source)
    }

    static <A,M extends Monad<A>> Maybe<A> Maybe(Object o, M source) {
        return Fnz.Maybe(source)
    }

    static <A> ListMonad<A> List(Object o, A... values) {
        return Fnz.List(values)
    }

    static <A> ListMonad<A> List(Object o, Iterable<A> values) {
        return Fnz.List(values)
    }

    static <A> Try.Success<A> Success(Object o, A source) {
        return Fnz.Success(source)
    }

    static <A> Try.Failure<A> Failure(Object o) {
        return Fnz.Failure()
    }

    static <A> Try.Failure<A> Failure(Object o, A a) {
        return Fnz.Failure(a)
    }

    public static <A,B> Try<B> Try(Object o, Function<A,B> fn) {
        return Fnz.Try(fn);
    }

    public static <A,B> Try<B> Try(Object o, A a, Function<A,B> fn) {
        return Fnz.Try(a, fn);
    }

    static <A,B,F extends Function<A,B>> Function<A, Try<B>> wrap(Object o, F fn) {
        return Fnz.wrap(fn)
    }

    static <A,B,MA extends Monad<A>,MB extends Monad<B>> MB bind(Object o, MA ma, Function<A,MB> fn) {
        return Fnz.bind(ma, fn)
    }

    static <A,B, AA extends Applicative<A>, AB extends Applicative<B>> AB fapply(
        Object o, AA fa, Applicative<Function<A,B>> fn) {
        return Fnz.fapply(fa, fn)
    }

    static <A,B, FA extends Functor<A>, FB extends Functor<B>> FB fmap(Object o, FA fa, Function<A,B> fn) {
        return Fnz.fmap(fa, fn)
    }

    static <A> A val(Object o, Monad<A> monad) {
        return Fnz.val(monad)
    }

    static <A,B> Function<A,Try<B>> recover(Object o, Closure<?>... alternatives) {
        return Fnz.recover(alternatives as Function<A,B>[]);
    }

    static <A,B, M extends Monad<B>> M bind(Iterable<A> col, Function<A, M> fn) {
        return col instanceof ListMonad ? col.bind(fn) : Fnz.List(col).bind(fn)
    }

    static <A,B, M extends Monad<B>> M bind2(Iterable<A> col, TypeAwareFunction<A, M> fn) {
        return col instanceof ListMonad ? col.bind2(fn) : Fnz.List(col).bind2(fn)
    }

    static <A,B> Applicative<B> fapply(Iterable<A> col, Applicative<Function<A, B>> afn) {
        return col instanceof ListMonad ? col.fapply(afn) : Fnz.List(col).fapply(afn)
    }

    static <A,B, F extends Functor<B>> F fmap(Iterable<A> col, Function<A, B> fn) {
        return col instanceof ListMonad  ? col.fmap(fn) : Fnz.List(col).fmap(fn)
    }

}
