# vmi - VIEW MODEL IT
VMI is a lightweightannotation based mvvm framework for vaadin in combination with spring boot. it comes with bunch of helpers for using mvvm.

I wrote that code because I wanted to use a light weight mvvm-framework like bambi for my spring boot application. 

Unfortunatly bambi is no longer supported by vaadin 8 as the container/property api does not exist anymore. 
This code enables you a two way data binding of your view to the corresponding viewModel. 

The overall structure borrows heavily from existing MVC/MVVM framework [bambi](https://raw.githubusercontent.com/michaeljfazio/bambi-mvvm/)
 
With using the ViewModelComposer.bind(...) method each field which is marked with a mvvm-binding annotation of your view is 
bind to the viewModel-Bean which name is defined in the @View(model="beanName") annotation. 

Annotation overview

| Annotations                                                                                                                              | Description                                                                                                                                                                                |
| ---------------------------------------------------------------------------------------------------------------------------------------- |:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:| 
| [@View](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/View.java)                         | Applied to a view class that shall be bound to a view model.                                                                                                                               |
| [@ItemBound](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/ItemBound.java)               | Applied to any UI element that implements the `HasValue` interface (e.g. `Form`). Binds the element to a corresponding `HasValue<V>` declared in the view model.                               |
| [@OnClick](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/OnClick.java)     | Applied to any UI element that extends the `Button` class. Binds the click event to the no arg method declared in the view model.                    |
| [@OnComponentEvent](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/OnComponentEvent.java)           |Applied to any UI element that extends `Component`. Binds all Component.Event events to a method in the corresponding view model.                                                                          |
| [@ListingBound](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/ListingBound.java)             | Applied to any UI element that implements HasItems<V>. Binds a specified field to a corresponding field that implements 'HasValue<List<V>> in the corresponding view model.                                                            |
 | [@SelectionBound](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/SelectionBound.java)             | Applied to any Grid Component. Binds all the selected item when an item gets selected                                                            |
  | [@EnableBound](https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/mvvm/EnableBound.java)             | Applied to any Grid Component. Binds all the selected item when an item gets selected                                                            |


``` java
# View

@View(model = "mainViewModel")
public class MainView extends CustomComponent{
 
    private final Registration registration;

    private final Label mWelcome = new Label("Book of visitors");

    @ItemBound(to = "name")
    private final TextField mNameTextField = new TextField();

    @ItemBound(to = "greeting")
    private final TextField mGreetingTextField = new TextField();

    @OnClick(method = "save")
    @EnableBound(to = "safeEnabled")
    private final Button mJoinCommunity = new Button("join");

    @ListingBound(to = "greetings")
    @SelectionBound(to = "selectedGreeting")
    private final Grid<Entry> mCommunityGrid = new Grid<>();

    @ItemBound(to = "selectedEntry")
    private final DetailComponent detailComponent;

    private final VerticalLayout mInputPanel = new VerticalLayout(
            this.mWelcome,
            this.mNameTextField,
            this.mGreetingTextField,
            this.mJoinCommunity
    );

    private final HorizontalLayout dataPanel = new HorizontalLayout(
            this.mInputPanel,
            this.mCommunityGrid
    );

    private final VerticalLayout layout = new VerticalLayout(
            this.dataPanel
    );

    public MainView(ViewModelComposer viewModelComposer){
           setCompositionRoot(this.layout);
           this.Registration = viewModelComposer.bind(this);
    }
    
}

#ViewModel
public class MainViewModel {
    private final EntryService service;

    private final Property<String> name = new Property<>("");
    private final Property<String> greeting = new Property<>("");
    private final Property<Entry> selectedEntry = new Property<>(new Entry());
    private final Property<String> selectedGreeting = new Property(""); 
    private final Property<Boolean> safeEnabled = new Property(Boolean.FALSE); 
    private final Property<List<Entry>> greetings = new Property(new ArrayList());

    public MainViewModel(EntryService service) {
        this.service = service;
        // For usage of AsyncTask view [AsyncTask|(https://github.com/flxplzk/vmi/blob/master/src/main/java/de/flxplzk/vaadin/async/AsyncTask.java)]
        new AsyncTask<>(params -> this.service.findAll())
               .withPostExecutionHandler(result -> this.greetings.setValue(result)
               .execute();
    }

    public void update(){
        this.service.save(this.selectedEntry.getValue());
    }

    public void save(){
        this.service.save(new Entry(name.getValue(), greeting.getValue()));
    }

    public void delete(){
        this.service.delete(this.selectedEntry.getValue());
    }
}
