```css
 _______               __                                        _______         __         
|_     _|.-----.-----.|  |_.---.-.-----.----.---.-.--------.    |   _   |.--.--.|  |_.-----.
 _|   |_ |     |__ --||   _|  _  |  _  |   _|  _  |        |    |       ||  |  ||   _|  _  |
|_______||__|__|_____||____|___._|___  |__| |___._|__|__|__|    |___|___||_____||____|_____|
                                 |_____|                                                    
 _______         __ __                                                                      
|    ___|.-----.|  |  |.-----.--.--.--.                                                     
|    ___||  _  ||  |  ||  _  |  |  |  |                                                     
|___|    |_____||__|__||_____|________|                                                     
                                                                                            
```
Yeah the name pretty much says it all. It's a bot that automatically follows everyone who instagram suggests to you.
you can run it in the command line or use it as a library. It runs using [Selenium](https://www.selenium.dev/) and the FireFox [Gecko Driver](https://github.com/mozilla/geckodriver)

## Command line
### Arguments:
\* = Required

| Name      | Aliases        | Description                                                              |
|-----------|----------------|--------------------------------------------------------------------------|
| help      | -h, --help     | Displays a list of program arguments and their descriptions              |
| *username | -u, --username | The username of the account to target                                    |
| *password | -p, --password | The passwor of the account to target                                    |
| log       | -l, --log      | A File to save logs to                                                   |
| quiet     | -q, --quiet    | Disable program logging                                                  |
| head      | -h, --head     | Run with the head of the browser window (Just dont...)                   |

## Library
### Running 
```java
String account = "example";
String password = "example";
IAF bot = new IAF(account, password);
bot.run();
```
