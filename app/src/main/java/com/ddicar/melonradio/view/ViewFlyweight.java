package com.ddicar.melonradio.view;

import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.History;

public class ViewFlyweight {

	public static final AboutView ABOUT = new AboutView();
	public static final ChatRoomsView CHATROOMS = new ChatRoomsView();
	public static final NewChatRoomView NEW_CHATROOM = new NewChatRoomView();
	public static final SearchChatRoomView SEARCH_CHATROOM = new SearchChatRoomView();

	public static final DairyChannelView DAIRY_CHANNEL = new DairyChannelView();
	public static final DairyView DAIRY = new DairyView();
	public static final FavoriteView FAVORITE = new FavoriteView();

	public static final ForgetPasswordView FORGET_PASSWORD = new ForgetPasswordView();
	public static final ResetPasswordView RESET_PASSWORD = new ResetPasswordView();
	public static final HardwareBindView HARDWARE_BIND = new HardwareBindView();
	public static final HelpView HELP = new HelpView();
	public static final HomeView HOME = new HomeView();
	public static final GuidanceView GUIDANCE = new GuidanceView();
	public static final LoginView LOGIN = new LoginView();

	public static final PersonalView PERSONAL = new PersonalView();
	public static final PhotoUploadView PHOTO_UPLOAD = new PhotoUploadView();
	public static final PlayView PLAY = new PlayView();
	public static final AbstractView REGISTER = new RegisterView();
	public static final SettingView SETTING = new SettingView();
	public static final SoftwareSettingView SOFTWARE_SETTING = new SoftwareSettingView();
	public static final HistoryView HISTORY = new HistoryView();
	public static final EmptyHistoryView EMPTY_HISTORY = new EmptyHistoryView();
	public static final QrCodeScanView QR_CODE_SCAN = new QrCodeScanView();
	public static final VehicleBrandsView VEHICLE_BRANDS = new VehicleBrandsView();
	public static final BaseInformationView BASE_INFORMATION = new BaseInformationView();


	public static final MainView MAIN_VIEW = new MainView();

	public static final MyView MY_VIEW = new MyView();
	public static final MapView MAP_VIEW = new MapView();
	public static final WaybillView WAY_BILL_VIEW = new WaybillView();
	public static final InformationView INFO_VIEW = new InformationView();


    public static final ContactListView CONTACT_LIST_VIEW = new ContactListView();
    public static final AddContactView ADD_CONTACT_VIEW = new AddContactView();
    public static final AddFromPhoneBookView ADD_FROM_PHONE_BOOK = new AddFromPhoneBookView();
    public static final CreateChatRoomView CREATE_CHAT_ROOM = new CreateChatRoomView();
    public static final ChatRoomView CHAT_ROOM = new ChatRoomView();
    public static final ContactView CONTACT_VIEW = new ContactView();
    public static final InviteView INVITE = new InviteView();
    public static final VerificationView VERIFICATION = new VerificationView();


    public static final NewFriendsView NEW_FRIENDS = new NewFriendsView();
    public static final InvitationView INVITATION = new InvitationView();
    public static final HistoryGroupView HISTORY_GROUP = new HistoryGroupView();

	public static final DriveTrackView DRIVE_TRACK = new DriveTrackView();
	public static final SettingsView SETTINGS = new SettingsView();
	public static final MyAccountView MY_ACCOUNT = new MyAccountView();

    public static final ChangePasswordView CHANGE_PASSWORD = new ChangePasswordView();
    public static final FeedbackView FEEDBACK = new FeedbackView();

	public static final InformationDetailView INFORMATION_DETAIL_VIEW = new InformationDetailView();

	public static void init() {

		ABOUT.init(R.layout.about);
		CHATROOMS.init(R.layout.chat_rooms);
		NEW_CHATROOM.init(R.layout.new_chat_room);
		SEARCH_CHATROOM.init(R.layout.search_chat_room);

		DAIRY.init(R.layout.dairy);
		DAIRY_CHANNEL.init(R.layout.dairy_channel);
		FAVORITE.init(R.layout.favorite);
		FEEDBACK.init(R.layout.feedback);
		FORGET_PASSWORD.init(R.layout.forget_password);
		RESET_PASSWORD.init(R.layout.reset_password);

		HARDWARE_BIND.init(R.layout.hardware_bind);
		HELP.init(R.layout.help1);
		HOME.init(R.layout.home);
		GUIDANCE.init(R.layout.guidance);
		LOGIN.init(R.layout.login);

		PERSONAL.init(R.layout.personal);
		PHOTO_UPLOAD.init(R.layout.photo_upload);
		PLAY.init(R.layout.play);
		REGISTER.init(R.layout.register);
		SOFTWARE_SETTING.init(R.layout.software_settings);
		SETTING.init(R.layout.setting);
		HISTORY.init(R.layout.history);
		EMPTY_HISTORY.init(R.layout.empty_history);
		QR_CODE_SCAN.init(R.layout.qr_code_scan);
		VEHICLE_BRANDS.init(R.layout.vehicle_brand);
		BASE_INFORMATION.init(R.layout.base_information);

		MAIN_VIEW.init(R.layout.main_view);
		MY_VIEW.init(R.layout.my);
		MAP_VIEW.init(R.layout.map);
		WAY_BILL_VIEW.init(R.layout.way_bill);
		INFO_VIEW.init(R.layout.information);

        CONTACT_LIST_VIEW.init(R.layout.contact_list);
        ADD_CONTACT_VIEW.init(R.layout.add_contact);
        ADD_FROM_PHONE_BOOK.init(R.layout.add_from_phone_book);
        CREATE_CHAT_ROOM.init(R.layout.create_chat_room);
        CHAT_ROOM.init(R.layout.chat_room);
        CONTACT_VIEW.init(R.layout.contact);

        INVITE.init(R.layout.invite);
        VERIFICATION.init(R.layout.verification);

        NEW_FRIENDS.init(R.layout.new_friends);
        INVITATION.init(R.layout.invitation_of_home);
        HISTORY_GROUP.init(R.layout.history_group);

        DRIVE_TRACK.init(R.layout.drive_track);
		SETTINGS.init(R.layout.settings);
		MY_ACCOUNT.init(R.layout.my_account);

        CHANGE_PASSWORD.init(R.layout.change_password);
        FEEDBACK.init(R.layout.feedback);

        INFORMATION_DETAIL_VIEW.init(R.layout.information_detail);
	}
}
