## LastDream - BoilerMake 2015 Hackathon Project
### A funny text based adventure game written in Scala  
###### Created By: Eric Aguilera <[@edawg878]>, Terry Lam <[@tlam6297]>, Christian Locke <[@lockchristian]>

### About the Game:  
The game begins with the player waking up on a typical day at Purdoo University.  
These ridiculous scenarios were inspired by real life college experiences!  

#### How it Works:
The Scala program parses a custom JSON story format and supports input from multiple story authors.

#### An Peek at our JSON Story Format:
```javascript
[
  {
    "id":"start",
    "prompt":[],
    "text":[
      "Your alarm goes off and you promptly wake up"
    ],
    "options":[
      "wakeup1",
      "wakeup2"
    ]
  },
  {
    "id":"wakeup2",
    "prompt":[
      "Go back to sleep"
    ],
    "text":[
      "Varoom can wait. Imma sleep",
      "*3 hours pass*",
      "You wake up again and realize that you have also missed English 106 and CS 191"
    ],
    "options":[
      "backsleep1",
      "backsleep2"
    ]
  }
]
```

#### Screenshot of Game:
![Screenshot1](/../screenshots/src/main/resources/img/screen1.png?raw=true "Example Gameplay")

  [@edawg878]: <https://github.com/EDawg878>
  [@tlam6297]: <https://github.com/tlam6297>
  [@lockchristian]: <https://github.com/LockChristian>
