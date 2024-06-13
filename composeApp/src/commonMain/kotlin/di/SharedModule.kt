package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val remoteModule= module {

}

val dataModule= module {



}

val domainModule= module {

}

val utilsModule= module {


}

val viewModelModule= module {

}

val sharedModules= listOf(
    remoteModule,
    dataModule,
    domainModule,
    utilsModule,
    viewModelModule,
    platformModule()
)

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    modules(sharedModules)
    appDeclaration()
}

fun initKoin() = initKoin { }
