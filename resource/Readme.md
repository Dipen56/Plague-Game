[Not an official readme, just some clarification about the level design so far. This will change]  
  
Here is my design, I may have changed our original design a little bit:  
  
========= about inventory and items =========  
  
Each player can carry maximum 6 antidotes and 6 items. Yes, two separate inventories.  
  
Items are keys (to open chest, or another area) and torches (to increase visibility at night) so far. There should be more interesting items. You are welcome to add ideas.  
  
Then I want to let the virus have several mutant types. Each potion of antidote you get can only take effect on a certain type of mutant.  
  
E.g. you pick up an antidote labelled as "Type A", and unfortunately the virus in your system is type B, so you either keep it in your inventory to hope you can find someone to trade it for a better use (and maybe you can trade back an antidote with type B), or you destroy it so others with type A virus has less chance to survive.  
  
I think this mechanism can encourage players interact with others more. (Or bring out the evilness....)  
  
=========== about time and day-night shift ========  
  
The world clock will start at 00:00, and then it will constantly advance, and never stops. Every player has one day left to live, that's equivalent 24 minutes in game (real time 1 second = game time 1 minute). We can get the extra marks if we do day-night shifting, and I reckon it's not hard, just when it's evening, the player's visibility reduces, and when it's daytime, it comes back.  
  
  
Each antidote of right type will give you 2 hours life, each antidote of wrong type will have 20% chance to give you 4 hours life, or 80% chance to reduce 2 hours of your time left. All these numbers are not hard-coded, so we can tweak it later easily if the difficulty level is not playable.  
  
========================================  
I'll post more details here as my code goes. Please let me know if someone dislike it.  