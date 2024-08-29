SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE if EXISTS tranx_journal_master_tbl;
CREATE TABLE tranx_journal_master_tbl (
  id BIGINT AUTO_INCREMENT NOT NULL,
   branch_id BIGINT NULL,
   outlet_id BIGINT NULL,
   fiscal_year_id BIGINT NULL,
   journal_no VARCHAR(255) NULL,
   journal_sr_no DOUBLE NULL,
   transcation_date date NULL,
   total_amt DOUBLE NULL,
   status BIT(1) NULL,
   narrations VARCHAR(255) NULL,
   financial_year VARCHAR(255) NULL,
   created_at datetime NULL,
   created_by BIGINT NULL,
   CONSTRAINT pk_tranx_journal_master_tbl PRIMARY KEY (id)
);

ALTER TABLE tranx_journal_master_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_MASTER_TBL_ON_BRANCH FOREIGN KEY (branch_id) REFERENCES branch_tbl (id);

ALTER TABLE tranx_journal_master_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_MASTER_TBL_ON_FISCAL_YEAR FOREIGN KEY (fiscal_year_id) REFERENCES fiscal_year_tbl (id);

ALTER TABLE tranx_journal_master_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_MASTER_TBL_ON_OUTLET FOREIGN KEY (outlet_id) REFERENCES outlet_tbl (id);

DROP TABLE if EXISTS tranx_journal_details_tbl;
CREATE TABLE tranx_journal_details_tbl (
  id BIGINT AUTO_INCREMENT NOT NULL,
   branch_id BIGINT NULL,
   outlet_id BIGINT NULL,
   ledger_id BIGINT NULL,
   tranx_journal_master_tbl BIGINT NULL,
   type VARCHAR(255) NULL,
   ledger_type VARCHAR(255) NULL,
   paid_amount DOUBLE NULL,
   created_at datetime NULL,
   created_by BIGINT NULL,
   status BIT(1) NULL,
   CONSTRAINT pk_tranx_journal_details_tbl PRIMARY KEY (id)
);

ALTER TABLE tranx_journal_details_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_DETAILS_TBL_ON_BRANCH FOREIGN KEY (branch_id) REFERENCES branch_tbl (id);

ALTER TABLE tranx_journal_details_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_DETAILS_TBL_ON_LEDGER FOREIGN KEY (ledger_id) REFERENCES ledger_master_tbl (id);

ALTER TABLE tranx_journal_details_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_DETAILS_TBL_ON_OUTLET FOREIGN KEY (outlet_id) REFERENCES outlet_tbl (id);

ALTER TABLE tranx_journal_details_tbl ADD CONSTRAINT FK_TRANX_JOURNAL_DETAILS_TBL_ON_TRANX_JOURNAL_MASTER_TBL FOREIGN KEY (tranx_journal_master_tbl) REFERENCES tranx_journal_master_tbl (id);
SET FOREIGN_KEY_CHECKS = 1;