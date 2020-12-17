# CurrentRestaurants

### Prepared explanation

- rxjava
- viewmodel factory and providers
- Paging source
- Use of FlatMaps, Observables, and translations through Singles

<img width="366" alt="Screen Shot 2020-12-17 at 4 00 23 PM" src="https://user-images.githubusercontent.com/7444521/102543300-160f6a80-4081-11eb-9369-37a99f462d7c.png">
<img width="362" alt="Screen Shot 2020-12-17 at 4 00 35 PM" src="https://user-images.githubusercontent.com/7444521/102543298-160f6a80-4081-11eb-8146-48225455e4ad.png">


### Clean Android Architecture
Clean architecture is the separation of concerns.  We want the “inner levels” meaning we start on the inside with the web/domain going through the use cases through the viewmodel to the view (outermost) to have no idea of what is going on in the classes that it calls across circles.

> The outer circle represents the concrete mechanisms that are specific to the platform such as networking and database access. Moving inward, each circle is more abstract and higher-level. The center circle is the most abstract and contains business logic, which doesn’t rely on the platform or the framework you’re using.

A unidirectional flow will ensure that because things are decoupled further and modularity remains a focus we will be able to increase the speed at which we add new features.  Librification becomes easier as well if we continue to scale the application and parts of our application need to be portable across apps.
Between our Use Case and domain we want to use interfaces for two reasons 

A Data layer which is of a higher, more abstract level doesn’t depend on a framework, lower-level layer.
The repository is an abstraction of Data Access and it does not depend on details. It depends on abstraction.

![final-architecture](https://user-images.githubusercontent.com/7444521/90908419-59432080-e3a2-11ea-9495-d9d42d1e640d.png)

#### In order to get an endless list we need to pagination and here are the problems it solves

- The list grows unbounded in memory, wasting memory as the user scrolls.
- We have to convert our results from Flow to LiveData to cache them, increasing the complexity of our code.
- If our app needed to show multiple lists, we'd see that there is a lot of boilerplate to write for each list.

Typically, your UI code observes a LiveData<PagedList> object (or, if you're using RxJava2, a Flowable<PagedList> or Observable<PagedList> object), which resides in your app's ViewModel. This observable object forms a connection between the presentation and contents of your app's list data.
  
In order to create one of these observable PagedList objects, pass in an instance of DataSource.Factory to a LivePagedListBuilder or RxPagedListBuilder object. A DataSource object loads pages for a single PagedList. The factory class creates new instances of PagedList in response to content updates, such as database table invalidations and network refreshes. The Room persistence library can provide DataSource.Factory objects for you, or you can build your own.
