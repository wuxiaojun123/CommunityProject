package cms.wxj.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cms.wxj.community.bean.BuildingListBean;
import cms.wxj.community.utils.CoordsGenerator;
import cms.wxj.community.view.CommunityView;

/**
 * Created by 54966 on 2018/6/20.
 */

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

	public static final int			MAX_BUILDING_NUM	= 151;

	private CommunityView			id_community;

	private TextView				id_tv_fuwei;

	private CoordsGenerator			coordsGenerator;

	private List<BuildingListBean>	list				= new ArrayList<>();

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);
		id_community = findViewById(R.id.id_community);
		id_tv_fuwei = findViewById(R.id.id_tv_fuwei);

		id_tv_fuwei.setOnClickListener(this);
		initCoordsGenerator();
		initBuildingListBean();
	}

	private void initBuildingListBean() {
		BuildingListBean buildingListBean = new BuildingListBean();
		buildingListBean.posX = 0;
		buildingListBean.posY = 0;
		buildingListBean.width = 1;
		buildingListBean.height = 2;
		list.add(buildingListBean);

		CoordsGenerator.Coords coords = coordsGenerator.getCoords(2);

		BuildingListBean buildingListBean2 = new BuildingListBean();
		buildingListBean2.posX = coords.getX();
		buildingListBean2.posY = coords.getY();
		buildingListBean2.width = 1;
		buildingListBean2.height = 1;
		list.add(buildingListBean2);

		CoordsGenerator.Coords coords3 = coordsGenerator.getCoords(3);

		BuildingListBean buildingListBean3 = new BuildingListBean();
		buildingListBean3.posX = coords3.getX();
		buildingListBean3.posY = coords3.getY();
		buildingListBean3.width = 1;
		buildingListBean3.height = 1;
		list.add(buildingListBean3);

		id_community.setBuildingListBeanList(list);
	}

	private void initCoordsGenerator() {
		coordsGenerator = new CoordsGenerator();
		coordsGenerator.init(MAX_BUILDING_NUM);
	}

	@Override public void onClick(View v) {
		id_community.fuwei();
	}


}
