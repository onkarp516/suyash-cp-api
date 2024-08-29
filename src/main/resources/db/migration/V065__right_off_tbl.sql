
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE if EXISTS right_off_tbl;
CREATE TABLE right_off_tbl (
  id BIGINT AUTO_INCREMENT NOT NULL,
   student_id BIGINT NULL,
   student_name VARCHAR(255) NULL,
   right_off_amt DOUBLE NULL,
   academic_year_id BIGINT NULL,
   academic_year VARCHAR(255) NULL,
   fiscal_year VARCHAR(255) NULL,
   standard_name VARCHAR(255) NULL,
   standard_id BIGINT NULL,
   right_off_note VARCHAR(255) NULL,
   created_by BIGINT NULL,
   updated_by BIGINT NULL,
   status BIT(1) NULL,
   created_at datetime NULL,
   updated_at datetime NULL,
   fees_transaction_summary_id BIGINT NULL,
   fees_head_id BIGINT NULL,
   sub_fees_head_id BIGINT NULL,
   branch_id BIGINT NULL,
   outlet_id BIGINT NULL,
   CONSTRAINT pk_right_off_tbl PRIMARY KEY (id)
);

SET FOREIGN_KEY_CHECKS = 1;