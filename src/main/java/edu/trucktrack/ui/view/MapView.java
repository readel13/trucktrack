package edu.trucktrack.ui.view;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import edu.trucktrack.api.dto.DriverDataForMapDTO;
import edu.trucktrack.dao.service.SecurityService;
import edu.trucktrack.service.MapService;
import edu.trucktrack.ui.MainLayout;

import edu.trucktrack.util.MapUpdater;
import jakarta.annotation.security.PermitAll;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//@StyleSheet("context://frontend/styles/google-maps/demo-styles.css")
@Route(value = "map", layout = MainLayout.class)
@PermitAll
public class MapView extends VerticalLayout{

	private ScheduledExecutorService executorService;
	private MapUpdater mapUpdater;
	private SecurityService securityService;
	private Long companyId;

	private MapService mapService;

	GoogleMap gmaps;
	UI ui = UI.getCurrent();
	private Map<String, GoogleMapMarker> mapMarkers = new HashMap<>();

	public MapView(MapUpdater mapUpdater, SecurityService securityService, MapService mapService) throws InterruptedException {
		this.setSizeFull();
		this.mapUpdater = mapUpdater;
		this.securityService = securityService;
		this.mapService = mapService;

		gmaps = new GoogleMap(this.mapService.getApiKey(), null, null);
		companyId = this.securityService.getUserData().getCompany().getId();
		loadMarkers();

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		createMenuBar();
		createGoogleMapsDemo();
		gmaps.goToCurrentLocation();

		startScheduledJob();
	}

	private void createGoogleMapsDemo() {
		gmaps.setMapType(GoogleMap.MapType.ROADMAP);
		gmaps.setSizeFull();
		if (!mapMarkers.isEmpty()) {
			mapMarkers.values().forEach(item -> gmaps.addMarker(item));
		}

		add(gmaps);
	}

	private void createMenuBar() {
		MenuBar menuBar = new MenuBar();
		MenuItem drivers = menuBar.addItem("Drivers");
		SubMenu driverList = drivers.getSubMenu();
		this.mapUpdater.getDriverData(companyId).forEach(item -> driverList.addItem(item.getDriverDataForMap().getDriverName(), e -> gmaps.setCenter(mapMarkers.get(item.getDriverDataForMap().getEmail()).getPosition())));
		add(menuBar);
	}

	private void loadMarkers() {
		this.mapUpdater.getDriverData(companyId).forEach(item -> {
			GoogleMapMarker mapMarker = new GoogleMapMarker(
					"Center",
					new LatLon(item.getLocationData().getLatitude(), item.getLocationData().getLongitude()),
					false,
					"https://www.flowingcode.com/wp-content/uploads/2020/06/FCMarker.png");

			H3 h3 = new H3(item.getDriverDataForMap().toString());
			mapMarker.addInfoWindow(h3.getElement().getOuterHTML());
			mapMarkers.put(item.getLocationData().getEmail(), mapMarker);
		});
	}

	private void startScheduledJob() {
		executorService = Executors.newSingleThreadScheduledExecutor();
		System.out.println("test");
		// Schedule a task to run every 5 seconds
		executorService.scheduleAtFixedRate(() -> {
			// Update UI component or perform any desired actions
			ui.access(() -> {
				Notification notification = Notification.show("");
				notification.setDuration(1000);

				mapUpdater.getDriverData(companyId).forEach(item -> {
					mapMarkers.get(item.getLocationData().getEmail()).setPosition(new LatLon(item.getLocationData()
							.getLatitude(), item.getLocationData().getLongitude()));
					mapMarkers.get(item.getLocationData().getEmail()).addInfoWindow(getLabelForMarker(item).getElement().getOuterHTML());
				});
			});
		}, 0, 1, TimeUnit.SECONDS);
	}

	private Component getLabelForMarker(DriverDataForMapDTO data) {
		Div verticalLayout = new Div();
		verticalLayout.add(new H3("driver name: " + data.getDriverDataForMap().getDriverName()));
		verticalLayout.add(new H3("truck name: " + data.getDriverDataForMap().getTruckName()));
		verticalLayout.add(new H3("truck number: " + data.getDriverDataForMap().getTruckNumber()));
		verticalLayout.add(new H3("cargos: " + data.getCargoNames()));
		verticalLayout.add(new H3("speed: " + data.getLocationData().getSpeed()));
		return verticalLayout;
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
		executorService.shutdownNow();
	}
}
