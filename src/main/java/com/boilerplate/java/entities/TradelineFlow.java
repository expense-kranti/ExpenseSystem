package com.boilerplate.java.entities;
/**
 * This is the enum list of tradeline flow.
 * @author love
 */
public enum TradelineFlow {
	Start,
	RequestAllocation,
	SenttoBank,
	Allocated,
	CMDCannotDeal,
	RequestBalance,
	BalanceReceived,
	RequestOfferApproval,
	OfferRaised,
	OfferApproved,
	OfferRejected,
	RequestPreNOCLetter,
	PreNOCReceived,
	RequestPaymentValidation,
	Payment,
	RequestPostNOCLetter,
	PostNOCLetterReceived,
	RequestCIBILUpdate,
	CIBILUpdateReceived,
	InvalidRequest,
	ZeroBalance,
	RequestBalanceRejected,
	RequestPreNOCLetterRejected,
	PaymentRejected,
	RequestPostNOCLetterRejected,
	RequestCIBILUpdateRejected,
	Broken,
	PaymentValidated
}
