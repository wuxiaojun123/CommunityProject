package cms.wxj.community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cms.wxj.community.view.CommunityView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private TextView	id_tv_community1;

	private TextView	id_tv_community2;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		id_tv_community1 = findViewById(R.id.id_tv_community1);
		id_tv_community1.setOnClickListener(this);

		id_tv_community2 = findViewById(R.id.id_tv_community2);
		id_tv_community2.setOnClickListener(this);
	}

	@Override public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.id_tv_community1:
				startActivity(new Intent(MainActivity.this, CommunityActivity.class));

				break;
			case R.id.id_tv_community2:
                startActivity(new Intent(MainActivity.this, CommunityActivity2.class));

				break;
		}
	}

}
