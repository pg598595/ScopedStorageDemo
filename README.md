# SocpedStorageDemo

What is Shared Storage ?

	Every app has there own private Directory in Internal Storage
	i.e android/data/your package name not visible to other apps
	Everything else is shared Storage is. Media Collection  and other folder
	App ask permission to have access to storage for simple functions like User to pick profile pic and download image etc. 	all this are left behind when the app is uninstalled. And user suffer from low disk space

SO to overcome this Android 10 comes with Redesigned Storage i.e Scoped Storage

What is Scoped Storage ?

	Divide the collection into specified Collections and limit the access to the whole storage 
		Better Attribution
			Which  app created which file 
			Useful when App is uninstalled ,all the data related to app is also uninstalled 
		Protecting App Data
			Internal app directories and external app directories are been private
		Protect user data
			Download image not to use by other app

Some Principles of Scopes Storage:

	Unrestricted access to your own app storage(Internal / External)
		Not Permission Required

	Unrestricted Access  to Media files and Download Collection
		Save image file Without Permission

	Only Media  Collection can be read with Storage Permission

	 Location MetaData ACESS_MEDIA_LOCATION for location of image 

	For Pdf , Etc  file Use System Picker 

	Reading and Writing outside of collection requires the system Picker


RequestLegacyAccess Tag 

	 -  In manifest file we can still add to use the permission access like in lower version than Android 10.But only used by 2% of android app, and also gonna soon deprecated in next version of android

Next Release Changes 

	Update to permission UI
	User will see different Permission UI based on update and using scoped Storage or not i.e. before 10 app would see board 		Access to app and after 10 media collection access to apps

	Enable File path and native libraries for reAding media
	updating Media file and modify APIS
	SPecial App access for selected Use cases(Google WhiteListed App )
	protecting External app Directories
	Enforcement to target SDk

	Read files not created by your app need READ_EXTRNAL_STORGAE Permission

	Edit and delete files not Contributed by your app , then you Need expercet user consent

	WRITE_EXTERNAL_STROGAE will be deprecated in next Android Release , and will give read permission only when used

	For Non Media file access

	To Access non Media file by other apps , to have access Launch the system picker 
	With SAF ,this will ask the user for permission and if user give permission full access is granted to that app.


ACCESS_MEDIA_LOCATION permission

	Runtime permission
	Not visible in settings 
	No guarantee you will have always have this permission , also if you have READ_EXTERNAL_STORAGE permission
	To get exact number of bytes of Files , Use MediaStore.setReqiueOriginal() , if not success  the exception occurs


Do/Don’ts For Storage

	Locked down file path access
	MediaStore Using recommend
	Media Store should be used Correctly , Don’t put Music in Picture directory
	Non media files should be placed in Download directory(recommend)

To Modify and Delete Media
 
	User Consent required when edit or delete media
	Consent required even for file path access
	Bulk edit delete in same dialog(Next Release)
	Copy and edit file, Copy to file into your App Directory and use


Special APP Access

	Only granted to app that can prove clear need to storage 
	Submit declaration form to Google Play
	WhiteListed apps by Google
	No access to external app directories 


Examples(Use Cases):

	Media Player App 
		Read all video files
		Mediastore Api 
		Content Resolver APi
		READ_EXTERNAL_STORAGE required

	Edit Image/Delete Image
		Media Store API 
		READ_EXTERNAL_STORAGE 
		Next Release -Bulk Delete in single dialog available

	GMAIL file attach
		SAF
		NO Permission required
		UI is handle by intent 
		To save file while sending you can use ACTION_CRATE_DOCUMENT





Notes

	ACTION_OPEN_DOCUMENT to select File

	ACTION_OPEN_DOCUMENT_TREE to select folder
	This will ask for the permission in Android 10 , for full access to that folder

	Access to Environment.DIRECTORY_PICTURES ...Media Collection requires Permission below android 10 

	Content access using raw file paths

	 Upon getting a document uri returned, we can use     [ContentResolver.takePersistableUriPermission] in order to persist the permission across restarts.

	If your app uses scoped storage, raw file path access is limited to the app-specific directories on external storage, even if your app has been granted the READ_EXTERNAL_STORAGE permission. If your app attempts to use a raw path to open a file within external storage that doesn't reside in the app-specific directory, a FileNotFoundException occurs. An example path for a file outside the app-specific directory is /sdcard/DCIM/IMG1024.JPG. Instead, your app should use methods in the MediaStore API.

	In [Build.VERSION_CODES.Q] and above, it isn't possible to modify or delete items in MediaStore directly, and explicit permission must usually be obtained to do this.The way it works is the OS will throw a [RecoverableSecurityException], which we  can catch here. Inside there's an [IntentSender] which the activity can use to prompt the user to grant permission to the item so it can be either updated or deleted.

