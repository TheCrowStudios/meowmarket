DELIMITER $$

create procedure createListing(in title varchar(80), description varchar(8000), quantityInStock int)
begin
insert into listings (title, description, quantityInStock) values (title, description, quantityInStock);
set id = last_insert_id();
end $$

create procedure addImageToListing(in imagePath varchar(255))
begin
insert into listingImages (listingId, imagePath)
end

DELIMITER ;