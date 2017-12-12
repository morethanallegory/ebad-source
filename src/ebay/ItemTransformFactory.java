package ebay;

import application.Application;

class ItemTransformFactory {

	static ItemTransform getTransform(){
		if(Application.switchedToCoUk()){
			return new ItemTransform();
		}
		return new USAItemTransform();
	}
}
