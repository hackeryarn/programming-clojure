(ns account)

(alias 'acc 'account)

(def test-savings {:id 1, :tag ::acc/savings, ::balance 100M})
(def test-checking {:id 2, :tag ::acc/checking, ::balance 250M})

(defmulti interest-rate :tag)
(defmethod interest-rate ::acc/checking [_] 0M)
(defmethod interest-rate ::acc/savings [_] 0.05M)

(defmulti account-level :tag)
(defmethod account-level ::acc/checking [acct]
  (if (>= (:balance acct) 5000) ::acc/premium ::acc/basic))
(defmethod account-level ::acc/savings [acct]
  (if (>= (:balance acct) 1000) ::acc/premium ::acc/basic))

(derive ::acc/savings ::acc/account)
(derive ::acc/checking ::acc/account)

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::acc/basic ::acc/checking] [_] 25)
(defmethod service-charge [::acc/basic ::acc/savings] [_] 10)
(defmethod service-charge [::acc/permium ::acc/account] [_] 0)

(service-charge {:tag ::acc/checking :balance 1000})

(service-charge {:tag ::acc/savings :balance 1000})
