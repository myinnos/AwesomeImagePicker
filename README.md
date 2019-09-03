# Awesome Image Picker
Awesome Image Picker library will pick images/gifs with beautiful interface. Supports image or gif, Single and Multiple Image selection.

 ![Awesome Image Picker - Example1](https://s19.postimg.org/4bxmouwbn/Image_Picker_example_1.png)
 `` ``
 ![Awesome Image Picker - Example2](https://s19.postimg.org/jlxhw1rtv/Image_Picker_example_2.png)
 `` ``
 ![Awesome Image Picker - Example3](https://s19.postimg.org/4ehibozz7/Image_Picker_example_3.png)
 `` ``
 ![Awesome Image Picker - Example4](https://s19.postimg.org/91nkdgnc3/Image_Picker_example_4.png)
 
#### Download Demo APK from [HERE](https://github.com/myinnos/AwesomeImagePicker/raw/master/apk/image-picker-demo.apk "APK")
  
#### Kindly use the following links to use this library:

In build.gradle (Project)
```java
allprojects {
  repositories {
			...
		maven { url "https://jitpack.io" }
	}
}
```
And then in the other gradle file(may be your app gradle or your own module library gradle, but never add in both of them to avoid conflict.)
```java
dependencies {
    compile 'com.github.myinnos:AwesomeImagePicker:1.0.2'
}
```
How to use
-----
**Step 1:** start intent to open awesome image picker gallery:
```java
Intent intent = new Intent(this, AlbumSelectActivity.class);
intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, <LIMIT>); // set limit for image selection
startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
```
**Step 2:** onActivityResult : [#Example](https://github.com/myinnos/AwesomeImagePicker/blob/master/app/src/main/java/in/myinnos/imagepicker/MainActivity.java "Example")
```java
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                // start play with image uri
                ..........
            }
        }
    }
```
**IMP Note:** Require STORAGE_PERMISSIONS if Build.VERSION.SDK_INT >= 23.
##### Any Queries? or Feedback, please let me know by opening a [new issue](https://github.com/myinnos/AwesomeImagePicker/issues/new)!

## Contact
#### Prabhakar Thota
* :globe_with_meridians: Website: [myinnos.in](http://www.myinnos.in "Prabhakar Thota")
* :email: e-mail: contact@myinnos.in
* :mag_right: LinkedIn: [PrabhakarThota](https://www.linkedin.com/in/prabhakarthota "Prabhakar Thota on LinkedIn")
* :thumbsup: Twitter: [@myinnos](https://twitter.com/myinnos "Prabhakar Thota on twitter")   
* :camera: Instagram: [@prabhakar_t_](https://www.instagram.com/prabhakar_t_/ "Prabhakar Thota on Instagram")   

> If you appreciate my work, consider buying me a cup of :coffee: to keep me recharged :metal: by [PayPal](https://www.paypal.me/fansfolio)

License
-------

    Copyright 2017 MyInnos

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
