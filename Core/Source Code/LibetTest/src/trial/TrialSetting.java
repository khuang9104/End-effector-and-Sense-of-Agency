package trial;

public class TrialSetting {
	
	// The modes of operation for the Libet trials.
	public final static int BUTTON_No_BEEP_Rec_BUTTON = 0;
	public final static int BUTTON_Plus_BEEP_Rec_BUTTON = 1;
	public final static int BUTTON_Plus_BEEP_Rec_BEEP = 2;
	public final static int BUTTON_No_ACTION_Rec_BEEP = 3;

	public final static int SENSOR_No_BEEP_Rec_FLEX = 4;
	public final static int SENSOR_Plus_BEEP_Rec_FLEX = 5;
	public final static int SENSOR_Plus_BEEP_Rec_BEEP = 6;
	public final static int SENSOR_No_ACTION_Rec_BEEP = 7;

	public final static String TYPE1 = "Button, no beep, record action time";
	public final static String TYPE2 = "Button and beep, record action time";
	public final static String TYPE3 = "Button and beep, record beep time";
	public final static String TYPE4 = "Just the beep, record beep time";

	public final static String TYPE5 = "Flex wrist, no beep, record action time";
	public final static String TYPE6 = "Flex wrist and beep, record action time";
	public final static String TYPE7 = "Flex wrist and beep, record beep time";
	public final static String TYPE8 = "Just the beep, record beep time";

	public static String[] TRIALTYPES = {TYPE1, TYPE2, TYPE3, TYPE4, TYPE5, TYPE6, TYPE7, TYPE8};
	public static int defaultBlockSize = 1;
	public static int[] defaultBlockOrder = {0,1,2,3,4,5,6,7};
	public static boolean drawTraces = false;
	public static boolean drawActionBlocker = false;
	public static int minInterval = 10;
}
