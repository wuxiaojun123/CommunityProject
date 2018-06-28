package cms.wxj.community;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cms.wxj.community.bean.BuildingListBean;
import cms.wxj.community.utils.CoordsGenerator;
import cms.wxj.community.view.CommunityView2;

/**
 * Created by 54966 on 2018/6/27.
 */

public class CommunityActivity2 extends Activity {

	public static final int			MAX_BUILDING_NUM	= 151;

	private CoordsGenerator			coordsGenerator;

	private List<BuildingListBean>	list				= new ArrayList<>();

	private CommunityView2			id_community2;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community2);

		id_community2 = findViewById(R.id.id_community2);
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

		BuildingListBean buildingListBean2 = null;
		for (int i = 0; i < 10; i++) {
			CoordsGenerator.Coords coords = coordsGenerator.getCoords(list.size() + 1);
			buildingListBean2 = new BuildingListBean();
			buildingListBean2.posX = coords.getX();
			buildingListBean2.posY = coords.getY();
			buildingListBean2.width = 1;
			buildingListBean2.height = 1;
//			list.add(buildingListBean2);
		}
		id_community2.setBuildingListBeanList(list);
	}

	private void initCoordsGenerator() {
		coordsGenerator = new CoordsGenerator();
		coordsGenerator.init(MAX_BUILDING_NUM);
	}

}
