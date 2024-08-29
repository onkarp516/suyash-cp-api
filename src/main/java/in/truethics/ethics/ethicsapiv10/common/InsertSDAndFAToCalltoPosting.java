package in.truethics.ethics.ethicsapiv10.common;

import com.google.gson.JsonObject;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerTransactionDetails;
import in.truethics.ethics.ethicsapiv10.model.master.LedgerMaster;
import in.truethics.ethics.ethicsapiv10.model.sales.TranxSalesInvoice;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerTransactionDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.sales_repository.TranxSalesInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InsertSDAndFAToCalltoPosting {
    @Autowired
    LedgerCommonPostings ledgerCommonPostings;

    @Autowired
    TranxSalesInvoiceRepository tranxSalesInvoiceRepository;
    @Autowired
    LedgerTransactionDetailsRepository transactionDetailsRepository;

    public JsonObject InsertStudentDataIntoCalltoPosting(LedgerMaster ledgerMaster)
    {
        JsonObject jsonObject=new JsonObject();
        if (ledgerMaster != null) {
            TranxSalesInvoice tranxSalesInvoice = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndOutletIdAndBranchIdAndStatus(ledgerMaster.getId(), ledgerMaster.getOutlet().getId(), ledgerMaster.getBranch().getId(),true);
            if (tranxSalesInvoice != null) {
                LedgerTransactionDetails transactionDetailsList = transactionDetailsRepository.findByLedgerMasterIdAndOutletIdAndBranchIdAndTransactionId(tranxSalesInvoice.getSundryDebtors().getId(),
                        tranxSalesInvoice.getSundryDebtors().getOutlet().getId(), tranxSalesInvoice.getSundryDebtors().getBranch().getId(), tranxSalesInvoice.getId());
                if (transactionDetailsList != null) {
                    ledgerCommonPostings.callToPostings(tranxSalesInvoice.getTotalAmount(), transactionDetailsList.getLedgerMaster(), transactionDetailsList.getTransactionType(), null,
                            tranxSalesInvoice.getFiscalYear(), tranxSalesInvoice.getBranch(), tranxSalesInvoice.getOutlet(), tranxSalesInvoice.getBillDate() != null ? tranxSalesInvoice.getBillDate() : null,
                            transactionDetailsList.getTransactionId(), tranxSalesInvoice.getSalesInvoiceNo(), "DR", true, transactionDetailsList.getTransactionType().getTransactionCode(), "create");
                    jsonObject.addProperty("message", "Posting against Student Done");
                }
            }

            /****** posting against Fees Head *****/
            List<LedgerTransactionDetails> transactionDetailsList = transactionDetailsRepository.findByFeesHeadofStudentagainstsalesInvoice(ledgerMaster.getId(),tranxSalesInvoice.getId());

            if(transactionDetailsList.size()>0) {

                for (LedgerTransactionDetails feetranxlist : transactionDetailsList) {
                    ledgerCommonPostings.callToPostings(feetranxlist.getCredit(), feetranxlist.getLedgerMaster(), feetranxlist.getTransactionType(), null,
                            tranxSalesInvoice.getFiscalYear(), tranxSalesInvoice.getBranch(), tranxSalesInvoice.getOutlet(), tranxSalesInvoice.getBillDate() != null ? tranxSalesInvoice.getBillDate() : null,
                            feetranxlist.getTransactionId(), tranxSalesInvoice.getSalesInvoiceNo(), "CR", true, feetranxlist.getTransactionType().getTransactionCode(), "create");
                    jsonObject.addProperty("messages", "Posting against FeesHead Done");
                }
            }

        }



        return jsonObject;
    }

}
