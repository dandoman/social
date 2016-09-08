CREATE TABLE blogs (
	id VARCHAR(100) NOT NULL,
    blog_url TEXT NOT NULL,
    instagram_handle VARCHAR(100),
    twitter_handle VARCHAR(100),
    email_address VARCHAR(100),
    
    PRIMARY KEY (id),
    UNIQUE(blog_url),
    UNIQUE(instagram_handle),
    UNIQUE(twitter_handle)
);

CREATE TABLE twitter_data (
	handle VARCHAR(100) NOT NULL,
	number_followers INTEGER NOT NULL,
	sample_date TIMESTAMP NOT NULL,	

	PRIMARY KEY (handle, sample_date),
	FOREIGN KEY (handle) REFERENCES blogs(twitter_handle)
);

CREATE TABLE instagram_data (
	handle VARCHAR(100) NOT NULL,
	number_followers INTEGER NOT NULL,
	sample_date TIMESTAMP NOT NULL,	

	PRIMARY KEY (handle, sample_date),
	FOREIGN KEY (handle) REFERENCES blogs(instagram_handle)
);

CREATE TABLE bloggers (
	id VARCHAR(100) NOT NULL,
    root_page_url TEXT NOT NULL,
	first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(100),
    last_crawled TIMESTAMP,
    
    PRIMARY KEY (id)
);

CREATE TABLE crawled_emails (
	blogger_id VARCHAR(100) NOT NULL,
	email_address VARCHAR(100) NOT NULL,
	url TEXT NOT NULL
	
	PRIMARY KEY (blogger_id, email_address),
	FOREIGN KEY (blogger_id) REFERENCES bloggers(id)
);

CREATE TABLE crawled_instagram_profiles (
	blogger_id VARCHAR(100) NOT NULL,
	handle VARCHAR(100) NOT NULL,
	url TEXT NOT NULL,
	
	PRIMARY KEY (blogger_id, handle),
	FOREIGN KEY (blogger_id) REFERENCES bloggers(id)
);

CREATE TABLE crawled_twitter_profiles (
	blogger_id VARCHAR(100) NOT NULL,
	handle VARCHAR(100) NOT NULL,
	url TEXT NOT NULL,
	
	PRIMARY KEY (blogger_id, handle),
	FOREIGN KEY (blogger_id) REFERENCES bloggers(id)
);

CREATE TABLE crawled_emails (
	blogger_id VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	url TEXT NOT NULL,
	
	PRIMARY KEY (blogger_id, email),
	FOREIGN KEY (blogger_id) REFERENCES bloggers(id)
);
