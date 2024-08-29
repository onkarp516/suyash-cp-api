SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE if EXISTS tranx_receipt_master_history_tbl;
CREATE TABLE tranx_receipt_master_history_tbl (
  id BIGINT AUTO_INCREMENT NOT NULL,
   tranx_receipt_master_id BIGINT NULL,
   branch_id BIGINT NULL,
   outlet_id BIGINT NULL,
   fiscal_year_id BIGINT NULL,
   receipt_sr_no DOUBLE NOT NULL,
   transaction_date date NULL,
   total_amt DOUBLE NOT NULL,
   status BIT(1) NOT NULL,
   narrations VARCHAR(255) NULL,
   financial_year VARCHAR(255) NULL,
   fee_receipt_no VARCHAR(255) NULL,
   operation_type VARCHAR(255) NULL,
   created_at datetime NULL,
   created_by BIGINT NULL,
   updated_at datetime NULL,
   updated_by BIGINT NULL,
   CONSTRAINT pk_tranx_receipt_master_history_tbl PRIMARY KEY (id)
);

DROP TABLE if EXISTS tranx_receipt_details_history_tbl;
CREATE TABLE tranx_receipt_details_history_tbl (
  id BIGINT AUTO_INCREMENT NOT NULL,
   tranx_receipt_details_id BIGINT NULL,
   branch_id BIGINT NULL,
   outlet_id BIGINT NULL,
   ledger_master_id BIGINT NULL,
   receipt_master_id BIGINT NULL,
   type VARCHAR(255) NULL,
   ledger_type VARCHAR(255) NULL,
   paid_amount DOUBLE NULL,
   operation_type VARCHAR(255) NULL,
   created_at datetime NULL,
   created_by BIGINT NULL,
   status BIT(1) NULL,
   CONSTRAINT pk_tranx_receipt_details_history_tbl PRIMARY KEY (id)
);

SET FOREIGN_KEY_CHECKS = 1;