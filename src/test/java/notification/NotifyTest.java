package notification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jy.dao.UserDao;
import com.jy.dao.UserLocationDao;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test.xml" })
public class NotifyTest {

	@Test
	public void test() {
		String accessId=UploadUtils.INSTANCE.getAndroidAccessId();
		String secretKey=UploadUtils.INSTANCE.getAndroidSecretKey();
		if (StringUtils.hasLength(accessId)) {
			XingeApp xinge = new XingeApp(Long.parseLong(accessId), secretKey);
			Message message = new Message();
			message = new Message();
			message.setType(Message.TYPE_NOTIFICATION);
			Style style = new Style(1);
			style = new Style(3,1,0,1,0);
			ClickAction action = new ClickAction();
			action.setActionType(ClickAction.TYPE_ACTIVITY);
			Map<String, Object> custom = new HashMap<String, Object>();
			message.setTitle("简约");
			message.setContent("test");
			message.setStyle(style);
			message.setAction(action);
			message.setCustom(custom);
			TimeInterval acceptTime1 = new TimeInterval(0,0,23,59);
			message.addAcceptTime(acceptTime1);
			System.out.println(xinge.pushSingleDevice("1ebd44ec2d3552374f3f81e3f2338ba5fe836765", message));
				
		}
	}

}
