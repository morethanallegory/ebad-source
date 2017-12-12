CREATE MEMORY TABLE ebayitems (
  id VARCHAR(150) NOT NULL ,
  category VARCHAR(250) NOT NULL ,
  wordcount INT DEFAULT NULL ,
  title VARCHAR(150)  DEFAULT NULL ,
  price VARCHAR(20)  DEFAULT NULL ,
  time_left VARCHAR(30)  DEFAULT NULL ,
  shipping VARCHAR(50)  DEFAULT NULL ,
  url VARCHAR(200)  DEFAULT NULL ,
  bids VARCHAR(20)  DEFAULT NULL ,
  image_url VARCHAR(200)  DEFAULT NULL ,
  update_time DATETIME  DEFAULT NULL ,
  PRIMARY KEY (id) 
  );
  
  CREATE MEMORY TABLE ebayitems_viewed (
  id VARCHAR(200) NOT NULL ,
  title VARCHAR(200)  DEFAULT NULL ,
  update_time DATETIME  DEFAULT NULL ,
  PRIMARY KEY (id) 
  );
  
  CREATE MEMORY TABLE counters (
   item_count INT DEFAULT 0 ,
   category_count INT DEFAULT 0 ,
   page_count INT DEFAULT 0 
  );
  


 