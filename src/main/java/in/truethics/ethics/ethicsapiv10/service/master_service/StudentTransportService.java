package in.truethics.ethics.ethicsapiv10.service.master_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import in.truethics.ethics.ethicsapiv10.common.LedgerCommonPostings;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerTransactionDetails;
import in.truethics.ethics.ethicsapiv10.model.ledgers_details.LedgerTransactionPostings;
import in.truethics.ethics.ethicsapiv10.model.master.Branch;
import in.truethics.ethics.ethicsapiv10.model.master.FiscalYear;
import in.truethics.ethics.ethicsapiv10.model.master.LedgerMaster;
import in.truethics.ethics.ethicsapiv10.model.master.TransactionTypeMaster;
import in.truethics.ethics.ethicsapiv10.model.sales.TranxSalesInvoice;
import in.truethics.ethics.ethicsapiv10.model.sales.TranxSalesInvoiceDetails;
import in.truethics.ethics.ethicsapiv10.model.school_master.*;
import in.truethics.ethics.ethicsapiv10.model.school_tranx.FeesTransactionSummary;
import in.truethics.ethics.ethicsapiv10.model.user.Users;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerBalanceSummaryRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerMasterRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerTransactionDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.ledgerdetails_repository.LedgerTransactionPostingsRepository;
import in.truethics.ethics.ethicsapiv10.repository.master_repository.*;
import in.truethics.ethics.ethicsapiv10.repository.student_tranx.FeesTransactionSummaryRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.sales_repository.TranxSalesInvoiceDetailsRepository;
import in.truethics.ethics.ethicsapiv10.repository.tranxs_repository.sales_repository.TranxSalesInvoiceRepository;
import in.truethics.ethics.ethicsapiv10.response.ResponseMessage;
import in.truethics.ethics.ethicsapiv10.util.JwtTokenUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentTransportService {

    private static final Logger studentTransportLogger = LoggerFactory.getLogger(StudentTransportService.class);
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    BusRepository busRepository;
    @Autowired
    private StudentTransportRepository studentTransportRepository;

    @Autowired
    private LedgerTransactionPostingsRepository ledgerTransactionPostingsRepository;
    @Autowired
    private LedgerCommonPostings ledgerCommonPostings;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private AcademicYearRepository academicYearRepository;
    @Autowired
    private StandardRepository standardRepository;
    @Autowired
    private StudentRegisterRepository studentRegisterRepository;

    @Autowired
    private StudentAdmissionRepository studentAdmissionRepository;
    @Autowired
    private FeesTransactionSummaryRepository feesTransactionSummaryRepository;
    @Autowired
    private FeesDetailsRepository feesDetailsRepository;
    @Autowired
    private LedgerTransactionDetailsRepository ledgerTransactionDetailsRepository;

    @Autowired
    private LedgerMasterRepository ledgerMasterRepository;
    @Autowired
    private TranxSalesInvoiceRepository tranxSalesInvoiceRepository;
    @Autowired
    private TransactionTypeMasterRepository transactionTypeMasterRepository;
    @Autowired
    private StudentAdmissionRepository admissionRepository;
    @Autowired
    private TranxSalesInvoiceDetailsRepository tranxSalesInvoiceDetailsRepository;
    @Autowired
    private LedgerBalanceSummaryRepository ledgerBalanceSummaryRepository;
    @Autowired
    private StudentRegisterService registerService;

    public JsonObject createStudentTransport(HttpServletRequest request) {
        JsonObject result = new JsonObject();
        Users users = jwtTokenUtil.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        try {
            boolean flag = false;
            String JsonToStr = request.getParameter("studentlist");
            JsonArray studentArray = new JsonParser().parse(JsonToStr).getAsJsonArray();
            List<StudentTransport> studentTransportList = new ArrayList<>();
            Long academicYearId = Long.valueOf(request.getParameter("academicYearId"));


            for (JsonElement jsonElement : studentArray) {
                JsonObject object = jsonElement.getAsJsonObject();
                if (object.get("id").getAsString() != null) {
                    StudentRegister studentRegister = studentRegisterRepository.findByIdAndStatus(object.get("id").getAsLong(), true);

                    if (studentRegister != null) {

                        StudentTransport std = studentTransportRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegister.getId(),academicYearId, true);

                        if (std == null) {
                            StudentTransport studentTransport = new StudentTransport();

                            AcademicYear academicYear = academicYearRepository.findByIdAndStatus(academicYearId, true);
                            studentTransport.setAcademicYear(academicYear);

//                        Long standardId = Long.valueOf(request.getParameter("standardId"));
//                        Standard standard = standardRepository.findByIdAndStatus(standardId, true);

                            studentTransport.setStudentType(Integer.valueOf(request.getParameter("studentType")));
//                        studentTransport.setStandard(standard);

                            Bus bus = busRepository.findByIdAndStatus(Long.valueOf(request.getParameter("busStopId")), true);
                            studentTransport.setBus(bus);

                            studentTransport.setStudentRegister(studentRegister);
                            studentTransport.setStatus(true);
                            studentTransport.setCreatedBy(users.getId());
                            studentTransport.setCreatedAt(LocalDateTime.now());

                            studentTransport.setOutlet(users.getOutlet());
                            studentTransport.setBranch(users.getBranch());

//                        studentTransportList.add(studentTransport);
                            StudentAdmission studentAdmission=studentAdmissionRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(studentRegister.getId(),academicYearId,true);

                            try {
                                StudentTransport studentTransport1 = studentTransportRepository.save(studentTransport);
                                if (studentTransport1 != null) {

                                    FeeHead busHead = null;
                                    FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStandardIdAndDivisionIdAndStatus(studentRegister.getId(),
                                            academicYearId,studentAdmission.getStandard().getId(),studentAdmission.getDivision().getId(),
                                            true);
                                    if (feesTransactionSummary != null) {

                                        /* If student set to particular bus route then it checks bus fee exist in fees master  */
                                        double headFee = 0;
                                        List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesTransactionSummary.getFeesMaster().getId(), true);
                                        for (FeesDetails details : feesDetailsList) {
                                            System.out.println("feesTransactionDetail.getFeeHead() ->>" + details.getFeeHead().getFeeHeadName());
                                            boolean isFound = details.getFeeHead().getFeeHeadName().toLowerCase().contains("bus"); // true
                                            System.out.println("Bus isFound => " + isFound);
                                            if (isFound) {
                                                busHead = details.getFeeHead();
                                                if (feesTransactionSummary.getStudentType() == 2 && feesTransactionSummary.getStudentGroup() != null && feesTransactionSummary.getStudentGroup() == 1
                                                        && (feesTransactionSummary.getStandard().getStandardName().equalsIgnoreCase("11") ||
                                                        feesTransactionSummary.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                                    if (feesTransactionSummary.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                                        headFee = details.getAmountForBoy();
                                                    } else if (feesTransactionSummary.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                                        headFee = details.getAmountForGirl();
                                                    }
                                                } else {
                                                    headFee = details.getAmount();
                                                }
                                            }
                                            System.out.println("headFee => " + headFee);
                                        }

                                        double busFee = bus.getBusFee(); // 8000

                                        double differenceAmount = headFee - busFee; // 12000 - 8000 = 4000

                                        double currentBalance = feesTransactionSummary.getBalance(); // 59000
                                        double totalFees = feesTransactionSummary.getTotalFees(); // 59000

                                        double netBalance = 0;
                                        double netFees = 0;
//                                        StudentAdmission studentAdmission = admissionRepository.findByStudentRegisterIdAndStatus(studentRegister.getId(), true);
//                                        if (studentAdmission.getIsBusConcession() == false) {
                                        netBalance = currentBalance + busFee; // 59000+8000 = 67000
                                        netFees = totalFees + busFee; // 59000+8000 = 67000
//                                        } else {
//                                            netBalance = currentBalance - differenceAmount; // 71000-4000 = 67000
//                                            netFees = totalFees - differenceAmount; // 71000-4000 = 67000
//                                        }
                                        feesTransactionSummary.setTotalFees(netFees);
                                        feesTransactionSummary.setBalance(netBalance);

                                        FeesTransactionSummary feesTransactionSummary1 = feesTransactionSummaryRepository.save(feesTransactionSummary);
                                        if (feesTransactionSummary1 != null) {

                                            LedgerMaster studentLedger = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                                                    studentRegister.getId(), users.getBranch().getId(), true);
                                            if (studentLedger != null) {

                                                TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
                                                TranxSalesInvoice mSalesTranx = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(studentLedger.getId(),academicYear.getFiscalYearId(),true);

                                                double outstanding = 0;
//                                                if (studentAdmission.getIsBusConcession() == false) { // student bus not applicable
                                                outstanding = mSalesTranx.getTotalAmount() + busFee;  // 41000 + 8000 = 49000
//                                                } else {
//                                                    outstanding = mSalesTranx.getTotalAmount() - differenceAmount;
//                                                }

                                                mSalesTranx.setTotalAmount(outstanding);
                                                mSalesTranx.setTotalBaseAmount(outstanding);
                                                mSalesTranx.setBalance(outstanding);
                                                mSalesTranx.setUpdatedAt(LocalDateTime.now());
                                                mSalesTranx.setUpdatedBy(users.getId());
                                                tranxSalesInvoiceRepository.save(mSalesTranx);

                                      /*          LedgerTransactionDetails ledgerTransactionDetails = ledgerTransactionDetailsRepository.
                                                        findByTransactionTypeIdAndLedgerMasterIdAndStatus(17L, studentLedger.getId(), true);
                                                if (ledgerTransactionDetails != null) {
                                                    ledgerTransactionDetails.setDebit(outstanding * -1);
                                                    ledgerTransactionDetails.setClosingBal(outstanding * -1);
                                                    ledgerTransactionDetailsRepository.save(ledgerTransactionDetails);
                                                }

                                                LedgerBalanceSummary ledgerBalanceSummary = ledgerBalanceSummaryRepository.findByLedgerMasterIdAndStatus(studentLedger.getId(), true);
                                                if (ledgerBalanceSummary != null) {
                                                    ledgerBalanceSummary.setDebit(outstanding * -1);
                                                    ledgerBalanceSummary.setClosingBal(outstanding * -1);
                                                    ledgerBalanceSummary.setBalance(outstanding * -1);
                                                    ledgerBalanceSummaryRepository.save(ledgerBalanceSummary);
                                                }*/
                                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.
                                                        findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(studentLedger.getId(),
                                                                17L, mSalesTranx.getId(), true);
                                                if (mLedger != null) {
                                                    mLedger.setAmount(outstanding);
                                                    mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                                    mLedger.setOperations("Updated");
                                                    ledgerTransactionPostingsRepository.save(mLedger);
                                                }
                                                /***** Update Sales Details *****/
                                                TranxSalesInvoiceDetails details = tranxSalesInvoiceDetailsRepository.findByStatusAndTranxSalesInvoiceIdAndFeeHeadId(
                                                        true, mSalesTranx.getId(), busHead.getId());
                                                if (details != null) {
                                                    details.setAmount(busFee);
                                                    tranxSalesInvoiceDetailsRepository.save(details);
                                                } else {
                                                    /******* Insert into Sales Invoice Details against Fees Head *****/
                                                    details = new TranxSalesInvoiceDetails();
                                                    details.setAmount(busFee);
                                                    if (mSalesTranx.getBranch() != null)
                                                        details.setBranch(mSalesTranx.getBranch());
                                                    details.setOutlet(mSalesTranx.getOutlet());
                                                    if (mSalesTranx.getFiscalYear() != null)
                                                        details.setFiscalYear(mSalesTranx.getFiscalYear());
                                                    details.setTranxSalesInvoice(mSalesTranx);
                                                    details.setFeeHead(busHead);
                                                    details.setStatus(true);
                                                    try {
                                                        tranxSalesInvoiceDetailsRepository.save(details);
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                        studentTransportLogger.error("Exception => while saving tranx sales invoice details in student transport" + ex);
                                                    }
                                                }

                                                LedgerTransactionDetails busTransactionDetail = ledgerTransactionDetailsRepository.
                                                        findByLedgerMasterIdAndOutletIdAndBranchIdAndTransactionIdAndStatus(
                                                        busHead.getLedger().getId(), users.getOutlet().getId(), users.getBranch().getId(), mSalesTranx.getId(), true);

                                                        LedgerTransactionPostings bustranxposting = ledgerTransactionPostingsRepository.
                                                                findByLedgerMasterIdAndOutletIdAndBranchIdAndTransactionIdAndTransactionTypeIdAndStatus(busHead.getLedger().getId(),
                                                                        users.getOutlet().getId(), users.getBranch().getId(), mSalesTranx.getId(), 17L,true);

//                                                If bus head ledger posting already not done then have to create
                                                if (bustranxposting == null) {
                                                    try {
//                                                        ledgerTransactionDetailsRepository.insertIntoLegerTranxDetail(
                                                /*        ledgerTransactionDetailsRepository.insertIntoLegerTranxDetailWithTranxDate(
                                                                mSalesTranx.getFeesAct().getFoundations().getId(),
                                                                mSalesTranx.getFeesAct().getPrinciples().getId(),
                                                                mSalesTranx.getFeesAct().getPrincipleGroups() != null ? mSalesTranx.getFeesAct().getPrincipleGroups().getId() : null,
                                                                null, tranxType.getId(), null,
                                                                mSalesTranx.getBranch() != null ? mSalesTranx.getBranch().getId() : null,
                                                                mSalesTranx.getOutlet().getId(), "pending", 0.0, busFee,
                                                                mSalesTranx.getBillDate() != null ? mSalesTranx.getBillDate() : null, null,
                                                                mSalesTranx.getId(), tranxType.getTransactionName() != null ? tranxType.getTransactionName() : null,
                                                                mSalesTranx.getFeesAct() != null ? mSalesTranx.getFeesAct().getUnder_prefix() : null,
                                                                mSalesTranx.getFinancialYear() != null ? mSalesTranx.getFinancialYear() : null, mSalesTranx.getCreatedBy(),
                                                                busHead.getLedger().getId(), mSalesTranx.getSalesInvoiceNo() != null ? mSalesTranx.getSalesInvoiceNo() : null, true);
                                                          */
                                                            ledgerCommonPostings.callToPostings(busFee,busHead.getLedger(),tranxType,null,mSalesTranx.getFiscalYear(),
                                                                    mSalesTranx.getBranch(),mSalesTranx.getOutlet(),mSalesTranx.getBillDate(),mSalesTranx.getId(),
                                                                    mSalesTranx.getSalesInvoiceNo() != null ? mSalesTranx.getSalesInvoiceNo() : null,"CR",true,tranxType.getTransactionCode(),"insert");

                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                        studentTransportLogger.error("Exception => while saving insertIntoLegerTranxDetailsPosting in student transport" + ex);
                                                    }
                                                } else {
                                                    try {
                                                     /*   ledgerTransactionDetailsRepository.ledgerPostingEdit(
                                                                busHead.getLedger().getId(), mSalesTranx.getId(), 17L, "CR", busFee);*/
                                                        bustranxposting.setAmount(busFee);
                                                        bustranxposting.setTransactionDate(mSalesTranx.getBillDate());
                                                        bustranxposting.setOperations("Updated");
                                                        ledgerTransactionPostingsRepository.save(bustranxposting);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        studentTransportLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                                    }
                                                }
                                            }
                                            flag = true;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                result.addProperty("message", "Internal Server Error");
                                result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
                                return result;
                            }
                        }
                    }
                }
            }
            if (flag) {
//                studentTransportRepository.saveAll(studentTransportList);
                result.addProperty("message", "Student Transportation assigned successfully");
                result.addProperty("responseStatus", HttpStatus.OK.value());
            } else {
                result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
                result.addProperty("message", "Internal Server Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.addProperty("message", "Internal Server Error");
        }
        return result;

    }


    public Object deleteStudentBusTransport(HttpServletRequest request) {
        ResponseMessage responseObject = new ResponseMessage();

        Long studentTransportId = Long.valueOf(request.getParameter("studentBusId"));
        Long studentId = Long.valueOf(request.getParameter("studentId"));
        Long academicYearId= Long.valueOf(request.getParameter("academicYearId"));

        try {
            Users users = jwtTokenUtil.getUserDataFromToken(request.getHeader("Authorization").substring(7));
            boolean flag = false;

            StudentTransport studentTransport = studentTransportRepository.findByIdAndStatus(studentTransportId, true);
            if (studentTransport != null) {
                String ay = studentTransport.getAcademicYear().getYear().split("-")[0] + "-";
                FiscalYear fiscalYear = registerService.getFiscalYearFromAcademicYear(ay);
                Double busFee = studentTransport.getBus().getBusFee();
                System.out.println("busFee " + busFee);

                FeeHead busHead = null;
                FeesTransactionSummary feesTransactionSummary = feesTransactionSummaryRepository.findByStudentRegisterIdAndAcademicYearIdAndStatus(
                        studentId,academicYearId,true);
                if (feesTransactionSummary != null) {
                    System.out.println(":feesTransactionSummary.getFeesMaster().getId()" + feesTransactionSummary.getFeesMaster().getId());
                    double headFee = 0;
                    List<FeesDetails> feesDetailsList = feesDetailsRepository.findByFeesMasterIdAndStatus(feesTransactionSummary.getFeesMaster().getId(), true);
                    for (FeesDetails feesDetails : feesDetailsList) {
//                        boolean isFound = feesDetails.getFeeHead().getFeeHeadName().equalsIgnoreCase("bus");
                        boolean isFound = feesDetails.getFeeHead().getFeeHeadName().toLowerCase().contains("bus"); // true
                        System.out.println("Bus isFound => " + isFound);
                        if (isFound) {
                            busHead = feesDetails.getFeeHead();
                            headFee = feesDetails.getAmount();
                            if (feesTransactionSummary.getStudentType() == 2 && (feesTransactionSummary.getStandard().getStandardName().equalsIgnoreCase("11") ||
                                    feesTransactionSummary.getStandard().getStandardName().equalsIgnoreCase("12"))) {
                                if (feesTransactionSummary.getStudentRegister().getGender().equalsIgnoreCase("male")) {
                                    headFee = feesDetails.getAmountForBoy();
                                } else if (feesTransactionSummary.getStudentRegister().getGender().equalsIgnoreCase("female")) {
                                    headFee = feesDetails.getAmountForGirl();
                                }
                            }
                        }
                        System.out.println("headFee => " + headFee);
                    }

                    if (busHead != null) {
                        double differenceAmount = headFee - busFee; // 12000 - 6000 = 6000
                        System.out.println("differenceAmount => " + differenceAmount);

                        double currentBalance = feesTransactionSummary.getBalance(); // 54000
                        System.out.println("currentBalance => " + currentBalance);
                        double totalFees = feesTransactionSummary.getTotalFees(); // 54000
                        System.out.println("totalFees => " + totalFees);

                        double netBalance = currentBalance - busFee; // 54000-6000
                        System.out.println("netBalance => " + netBalance);
                        double netFees = totalFees - busFee; // 54000-6000
                        System.out.println("netFees => " + netFees);
                        feesTransactionSummary.setTotalFees(netFees);
                        feesTransactionSummary.setBalance(netBalance);

                        FeesTransactionSummary feesTransactionSummary1 = feesTransactionSummaryRepository.save(feesTransactionSummary);
                        if (feesTransactionSummary1 != null) {

                            LedgerMaster studentLedger = ledgerMasterRepository.findByStudentRegisterIdAndBranchIdAndStatus(
                                    feesTransactionSummary1.getStudentRegister().getId(), users.getBranch().getId(), true);
                            if (studentLedger != null) {

                                TransactionTypeMaster tranxType = transactionTypeMasterRepository.findByTransactionCodeIgnoreCase("feestr");
                                TranxSalesInvoice mSalesTranx = tranxSalesInvoiceRepository.findBySundryDebtorsIdAndFiscalYearIdAndStatus(
                                        studentLedger.getId(), fiscalYear.getId(), true);

                                double outstanding = mSalesTranx.getTotalAmount() - busFee;  // 49000 - 8000 = 41000

                                mSalesTranx.setTotalAmount(outstanding);
                                mSalesTranx.setTotalBaseAmount(outstanding);
                                mSalesTranx.setBalance(outstanding);
                                mSalesTranx.setUpdatedAt(LocalDateTime.now());
                                mSalesTranx.setUpdatedBy(users.getId());
                                mSalesTranx.setOperations("updated");
                                tranxSalesInvoiceRepository.save(mSalesTranx);

                                LedgerTransactionPostings mLedger = ledgerTransactionPostingsRepository.findByLedgerMasterIdAndTransactionTypeIdAndTransactionIdAndStatus(
                                        studentLedger.getId(), 17L, mSalesTranx.getId(), true);
                                if (mLedger != null) {
                                    mLedger.setAmount(outstanding);
                                    mLedger.setTransactionDate(mSalesTranx.getBillDate());
                                    mLedger.setOperations("Updated");
                                    ledgerTransactionPostingsRepository.save(mLedger);
                                }

//                                ledgerCommonPostings.callToPostings(outstanding,  studentLedger,tranxType , studentLedger.getAssociateGroups(),
//                                        mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(),
//                                        LocalDate.now(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(),
//                                        "DR", true, tranxType.getTransactionCode(), "Delete");

//                                LedgerTransactionDetails ledgerTransactionDetails = ledgerTransactionDetailsRepository.findByTransactionTypeIdAndLedgerMasterIdAndStatus(
//                                        17L, studentLedger.getId(), true);
//                                if (ledgerTransactionDetails != null) {
//                                    ledgerTransactionDetails.setDebit(outstanding * -1);
//                                    ledgerTransactionDetails.setClosingBal(outstanding * -1);

//                                    ledgerTransactionDetailsRepository.save(ledgerTransactionDetails);
//                                }

                               /* LedgerBalanceSummary ledgerBalanceSummary = ledgerBalanceSummaryRepository.findByLedgerMasterIdAndStatus(studentLedger.getId(), true);
                                if (ledgerBalanceSummary != null) {
                                    ledgerBalanceSummary.setDebit(outstanding * -1);
                                    ledgerBalanceSummary.setClosingBal(outstanding * -1);
                                    ledgerBalanceSummary.setBalance(outstanding * -1);
                                    ledgerBalanceSummaryRepository.save(ledgerBalanceSummary);
                                }*/

                                if (busHead != null) {
                                    /***** Update Sales Details *****/
                                    TranxSalesInvoiceDetails details = tranxSalesInvoiceDetailsRepository.findByStatusAndTranxSalesInvoiceIdAndFeeHeadId(
                                            true, mSalesTranx.getId(), busHead.getId());
                                    if (details != null) {
                                        details.setAmount(0.0);
                                        details.setStatus(false);
                                        tranxSalesInvoiceDetailsRepository.save(details);

                                        try {
                                           /* ledgerTransactionDetailsRepository.ledgerPostingEdit(
                                                    busHead.getLedger().getId(), mSalesTranx.getId(), 17L, "CR", 0.0);*/

                                            ledgerCommonPostings.callToPostings(busFee, busHead.getLedger(),tranxType , busHead.getLedger().getAssociateGroups(),
                                                    mSalesTranx.getFiscalYear(), mSalesTranx.getBranch(), mSalesTranx.getOutlet(),
                                                    LocalDate.now(), mSalesTranx.getId(), mSalesTranx.getSalesInvoiceNo(),
                                                    "DR", true, tranxType.getTransactionCode(), "Delete");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            studentTransportLogger.error("Exception => while saving ledgerPostingEdit in student transport" + e);
                                        }
                                    }
                                }
                            }
                            flag = true;
                        }
                    }
                    studentRegisterRepository.deleteStudentBusId(studentTransportId);
                    responseObject.setResponseStatus(HttpStatus.OK.value());
                    responseObject.setMessage("Student transport deleted successfully");
                } else {
                    studentRegisterRepository.deleteStudentBusId(studentTransportId);
                    responseObject.setResponseStatus(HttpStatus.OK.value());
                    responseObject.setMessage("Student transport deleted successfully");
                }
            }
            return responseObject;
        } catch (
                Exception e) {
            e.printStackTrace();
            responseObject.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseObject.setMessage("Duplicate Student Records in FeesTransactionSummary");

        }
        return responseObject;
    }


    public JsonObject getBusAllocatedStudentList(HttpServletRequest request) {

        JsonObject result = new JsonObject();
        Users users = jwtTokenUtil.getUserDataFromToken(request.getHeader("Authorization").substring(7));
        JsonArray res = new JsonArray();
        try {

            String academicYearId = request.getParameter("academicYearId");
            String standardId = request.getParameter("standardId");
//            long acdemicfiscalid= Long.parseLong(request.getHeader("academic-year-id"));

            List<StudentTransport> list = new ArrayList<>();
            if (!academicYearId.equalsIgnoreCase("") && !standardId.equalsIgnoreCase("")) {
                list = studentTransportRepository.getDataByAcademicAndStandard(users.getOutlet().getId(), users.getBranch().getId(), academicYearId, standardId, true);
            } else if (!academicYearId.equalsIgnoreCase("")) {
                list = studentTransportRepository.getDataByAcademic(users.getOutlet().getId(), users.getBranch().getId(), academicYearId, true);
            } else if (!standardId.equalsIgnoreCase("")) {
                list = studentTransportRepository.getDataByStandard(users.getOutlet().getId(), users.getBranch().getId(), standardId, true);
            } else {
//                list = studentTransportRepository.findByOutletIdAndBranchIdAndAcademicYearIdAndStatus(users.getOutlet().getId(), users.getBranch().getId(), acdemicfiscalid ,true);
                list = studentTransportRepository.findByOutletIdAndBranchIdAndStatus(users.getOutlet().getId(), users.getBranch().getId(),true);

            }

            for (StudentTransport studentTransport : list) {
                JsonObject response = new JsonObject();
//                    response.addProperty("standardName",studentTransport.getStandard().getStandardName());
                response.addProperty("year", studentTransport.getAcademicYear().getYear());
                response.addProperty("academicyearId",studentTransport.getAcademicYear().getId());
                response.addProperty("id", studentTransport.getId());
                response.addProperty("studentId", studentTransport.getStudentRegister().getId());
                response.addProperty("busStop", studentTransport.getBus().getBusStopName());
                response.addProperty("busFee", studentTransport.getBus().getBusFee());

                response.addProperty("firstName", studentTransport.getStudentRegister().getFirstName());
                response.addProperty("middleName", studentTransport.getStudentRegister().getMiddleName());
                response.addProperty("lastName", studentTransport.getStudentRegister().getLastName());
                response.addProperty("studentName", studentTransport.getStudentRegister().getLastName() + " " + studentTransport.getStudentRegister().getFirstName());

                res.add(response);
            }

            result.add("responseObject", res);
            result.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {

            e.printStackTrace();
            result.addProperty("message", "Failed to load data");
            result.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return result;


    }

    public InputStream sheetLoad(Map<String, String> request, HttpServletRequest req) {
        String[] HEADERs = {"ID", "FULL NAME", "ACADEMIC YEAR", "BUS STOP NAME", "BUS STOP FEES"};

        String standardId = request.get("standardId");
        String standardName = "";
        if (!standardId.equalsIgnoreCase("")) {
            Standard standard = standardRepository.findByIdAndStatus(Long.parseLong(standardId), true);
            if (standard != null) {
                standardName = standard.getStandardName();
            }
        }

        String branchId = request.get("branchId");
        String branchName = "";
        if (!branchId.equalsIgnoreCase("")) {
            Branch branch = branchRepository.findByIdAndStatus(Long.parseLong(branchId), true);
            if (branch != null) {
                branchName = branch.getBranchName();
            }
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Transport List");
            Row headerRow = sheet.createRow(3);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            Row row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("BRANCH");
            row1.createCell(1).setCellValue(branchName);

            Row row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("STANDARD");
            row2.createCell(1).setCellValue(standardName);


            String JsonTostr = request.get("studentList");
            JsonArray studentArray = new JsonParser().parse(JsonTostr).getAsJsonArray();


            int rowIdx = 4;
            for (int i = 0; i < studentArray.size(); i++) {
                JsonObject studObj = studentArray.get(i).getAsJsonObject();
                Row row = sheet.createRow(rowIdx++);
                try {
                    row.createCell(0).setCellValue(studObj.get("studentId").getAsInt());
                    row.createCell(1).setCellValue(studObj.get("studentName").getAsString());
                    row.createCell(2).setCellValue(studObj.get("year").getAsString());
                    row.createCell(3).setCellValue(studObj.get("busStop").getAsString());
                    row.createCell(4).setCellValue(studObj.get("busFee").getAsDouble());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception" + e.getMessage());
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException e ===>" + e.getMessage());
            throw new RuntimeException("failed to import Data to Excel File" + e.getMessage());
        }
    }
}
