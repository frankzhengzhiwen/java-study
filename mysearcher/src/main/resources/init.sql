-- 文件信息表
DROP TABLE IF EXISTS file_meta;
CREATE TABLE IF NOT EXISTS file_meta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    path VARCHAR(1000) NOT NULL,
    size BIGINT NOT NULL,
    last_modified TIMESTAMP NOT NULL
);

-- 文件拼音表
DROP TABLE IF EXISTS file_pinyin;
CREATE TABLE IF NOT EXISTS file_pinyin(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    value VARCHAR(50) NOT NULL,
    file_meta_id INTEGER,
    FOREIGN KEY (file_meta_id) REFERENCES file_meta(id)
);