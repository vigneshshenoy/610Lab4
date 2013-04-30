package vkshenoy.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vkshenoy_transaction")
public class Transaction {
	
	@Id
	@Column(nullable=false)
	private long tid;
	
	private String operation;
	
	private String eventName;
	
	@Column(columnDefinition="longblob")
	private byte[] transactionData;
	
	public Transaction() {
		
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public byte[] getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(byte[] transactionData) {
		this.transactionData = transactionData;
	}

}
