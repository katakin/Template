package ru.katakin.sample.ui;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import ru.katakin.sample.Constants;
import ru.katakin.sample.R;
import ru.katakin.sample.model.Data;
import ru.katakin.sample.retrofit.GlobalCallback;
import ru.katakin.sample.retrofit.Manager;
import ru.katakin.sample.retrofit.MyCallback;
import ru.katakin.sample.util.LocationManager;

public class MainActivity extends BaseActivity implements View.OnClickListener, GlobalCallback {

    private ImageView icon;
    private TextView temperature;
    private TextView city_name;
    private EditText edit_city;
    private Button request;

    private LocationManager locationManager;
    private EventBus bus;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon = (ImageView) this.findViewById(R.id.icon);
        temperature = (TextView) this.findViewById(R.id.temperature);
        city_name = (TextView) this.findViewById(R.id.city_name);
        edit_city = (EditText) this.findViewById(R.id.edit_city);
        request = (Button) this.findViewById(R.id.request);
        request.setOnClickListener(this);

        bus = EventBus.getDefault();

        manager = Manager.getInstance(getApplicationContext());

        locationManager = LocationManager.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request:
                if (TextUtils.isEmpty(edit_city.getText().toString())) {
                    locationManager.startLocationUpdates();
                } else {
                    manager.getWeatherByName(edit_city.getText().toString(), new MyCallback<Data>(MainActivity.this, this, true, Constants.SERVICE_MODE.NAME));
                }
                break;
        }
    }

    @Subscribe
    public void onEvent(Location location) {
        manager.getWeatherByGeo(location, new MyCallback<Data>(MainActivity.this, this, true, Constants.SERVICE_MODE.GEO));
    }

    @Override
    public void onFailure(final String error, final Constants.SERVICE_MODE mode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case GEO:
                    case NAME:
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onSuccess(final Object object, final Constants.SERVICE_MODE mode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case GEO:
                    case NAME:
                        Data data = (Data) object;
                        int celsius = (int) (data.getMain().getTemp() - 273.15);
                        temperature.setText(String.valueOf(celsius));
                        city_name.setText(data.getName());
                        Picasso.with(MainActivity.this)
                                .load(Constants.ICON_URL + data.getWeather().get(0).getIcon() + ".png")
                                .into(icon);
                        break;
                }
            }
        });
    }
}
