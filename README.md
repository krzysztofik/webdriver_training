webdriver_training
==================
If FirefoxDriver cant find your firefox binary you can use this system property:
-Dwebdriver.firefox.bin="{absolute_path_to_your_folder_with_firefox}\firefox.exe"

In case of using Chrome you need to give path to chromedriver binary:
-Dwebdriver.chrome.driver="{absolute_path_to_your_folder_with_chromdriver}\chromedriver.exe"