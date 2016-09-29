-- テスト用親子テーブル
CREATE TABLE organization (
   organization_id int(10) unsigned NOT NULL,
   organization_name text NOT NULL,
   PRIMARY KEY (organization_id)
);

CREATE TABLE openjdk_people (
   id int(10) unsigned NOT NULL AUTO_INCREMENT,
   username text NOT NULL,
   full_name text DEFAULT NULL,
   organization_id int(10) unsigned DEFAULT NULL,
   blog text DEFAULT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_1 FOREIGN KEY (organization_id) 
   REFERENCES organization (organization_id) 
   ON UPDATE CASCADE
);
